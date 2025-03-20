package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.TokenDto;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.exception.DatabaseException;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import com.tarikkamat.taskmanagement.requests.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private LoginRequest loginRequest;
    private UserDto registerUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");

        loginRequest = new LoginRequest("test@example.com", "password");
        registerUserDto = new UserDto(null, null, "test@example.com", "testuser", "password", null);
    }

    @Test
    void authenticate_Success() {
        // Arrange
        when(userService.findByEmailOrUsername(anyString())).thenReturn(testUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken(testUser, "password"));
        when(jwtService.generateToken(testUser)).thenReturn("test-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        // Act
        TokenDto response = authService.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("test-token", response.accessToken());
        assertEquals(3600L, response.expirationIn());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticate_UserNotFound() {
        // Arrange
        when(userService.findByEmailOrUsername(anyString()))
            .thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.authenticate(loginRequest));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void register_Success() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(userService).createUser(any());

        // Act & Assert
        assertDoesNotThrow(() -> authService.register(registerUserDto));
        verify(userService).createUser(any());
    }

    @Test
    void register_DuplicateUser() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doThrow(DataIntegrityViolationException.class).when(userService).createUser(any());

        // Act & Assert
        assertThrows(DatabaseException.class, () -> authService.register(registerUserDto));
    }
} 