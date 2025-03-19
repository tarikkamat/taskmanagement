package com.tarikkamat.taskmanagement.controller;

import com.tarikkamat.taskmanagement.dto.user.AuthenticateDto;
import com.tarikkamat.taskmanagement.dto.user.LoginResponse;
import com.tarikkamat.taskmanagement.dto.user.RegisterDto;
import com.tarikkamat.taskmanagement.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void authenticate_Success() throws Exception {
        // Arrange
        AuthenticateDto authenticateDto = new AuthenticateDto("test@example.com", "password");
        LoginResponse loginResponse = new LoginResponse("test-token", 3600L);
        when(authService.authenticate(any(AuthenticateDto.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"identifier\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.accessToken").value("test-token"))
                .andExpect(jsonPath("$.data.expirationIn").value(3600));
    }

    @Test
    void register_Success() throws Exception {
        // Arrange
        RegisterDto registerDto = new RegisterDto("testuser", "test@example.com", "password");

        // Act & Assert
        mockMvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Registration successful"));
    }
} 