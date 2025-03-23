package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.user.LoginRequest;
import com.tarikkamat.taskmanagement.api.requests.user.RegisterRequest;
import com.tarikkamat.taskmanagement.dto.TokenDto;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.Role;
import com.tarikkamat.taskmanagement.exception.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private User user;
    private UserDto userDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        // Test için UUID oluştur
        userId = UUID.randomUUID();
        
        // Login isteği için test verisi
        loginRequest = new LoginRequest("testuser", "password123");

        // Kayıt isteği için test verisi
        registerRequest = new RegisterRequest("Test User", "test@example.com", "testuser", "password123");

        // Kullanıcı nesnesi
        user = new User();
        user.setId(userId);
        user.setFullName("Test User");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.TEAM_MEMBER);

        // UserDto nesnesi
        userDto = new UserDto(userId, "Test User", "test@example.com", "testuser", "encodedPassword", Role.TEAM_MEMBER);
    }

    @Test
    void authenticate_ShouldReturnTokenDto() {
        // Mock ayarları
        when(userService.findByEmailOrUsername("testuser")).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600000L);

        // Test
        TokenDto result = authService.authenticate(loginRequest);

        // Doğrulama
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertNotNull(result);
        assertEquals("jwt-token", result.accessToken());
        assertEquals(3600000L, result.expirationIn());
    }

    @Test
    void register_ShouldCreateUser() {
        // Mock ayarları
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userService.toDto(any(User.class))).thenReturn(userDto);

        // Test
        authService.register(registerRequest);

        // Doğrulama
        verify(userService).createUser(any(UserDto.class));
    }

    @Test
    void register_ShouldThrowDatabaseException_WhenDataIntegrityViolationOccurs() {
        // Mock ayarları
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userService.toDto(any(User.class))).thenReturn(userDto);
        doThrow(DataIntegrityViolationException.class).when(userService).createUser(any(UserDto.class));

        // Test ve doğrulama
        assertThrows(DatabaseException.class, () -> authService.register(registerRequest));
    }
} 