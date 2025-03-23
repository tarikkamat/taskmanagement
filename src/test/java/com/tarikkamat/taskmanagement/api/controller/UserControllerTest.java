package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.api.requests.user.ProjectAssignmentRequest;
import com.tarikkamat.taskmanagement.api.requests.user.RoleAssignmentRequest;
import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.enums.Role;
import com.tarikkamat.taskmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private UUID userId;
    private UUID projectId;
    private Date now;
    private UserDto userDto;
    private RoleAssignmentRequest roleAssignmentRequest;
    private ProjectAssignmentRequest projectAssignmentRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userId = UUID.randomUUID();
        projectId = UUID.randomUUID();
        now = new Date();

        // Test verilerini oluştur
        userDto = new UserDto(
                userId,
                "Test User",
                "test@example.com",
                "testuser",
                "testPassword",
                Role.TEAM_MEMBER
        );

        roleAssignmentRequest = new RoleAssignmentRequest(Role.TEAM_LEADER);
        projectAssignmentRequest = new ProjectAssignmentRequest(projectId);
    }

    @Test
    void getAllUsers_Success() throws Exception {
        // Arrange
        List<UserDto> users = Arrays.asList(userDto);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Users found"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].username").value("testuser"));
    }

    @Test
    void getUserByUsername_Success() throws Exception {
        // Arrange
        when(userService.getUserByUsername("testuser")).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{username}", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("User found"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void assignUserToDepartment_Success() throws Exception {
        // Arrange
        doNothing().when(userService).assignUserToDepartment("testuser");

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{username}/department", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Department updated"))
                .andExpect(jsonPath("$.httpStatusCode").value(200));

        verify(userService).assignUserToDepartment("testuser");
    }

    @Test
    void assignUserToRole_Success() throws Exception {
        // Arrange
        doNothing().when(userService).assignUserToRole(eq("testuser"), any(RoleAssignmentRequest.class));

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{username}/role", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"TEAM_LEADER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Role updated"))
                .andExpect(jsonPath("$.httpStatusCode").value(200));

        verify(userService).assignUserToRole(eq("testuser"), any(RoleAssignmentRequest.class));
    }

    @Test
    void assignUserToProject_Success() throws Exception {
        // Arrange
        doNothing().when(userService).assignUserToProject(eq("testuser"), any(ProjectAssignmentRequest.class));

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{username}/project", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\":\"" + projectId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Project updated"))
                .andExpect(jsonPath("$.httpStatusCode").value(200));

        verify(userService).assignUserToProject(eq("testuser"), any(ProjectAssignmentRequest.class));
    }

    @Test
    void getAllUsers_NotFound() throws Exception {
        // Arrange
        when(userService.getAllUsers())
                .thenThrow(new RuntimeException("Users not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Users not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }

    @Test
    void getUserByUsername_NotFound() throws Exception {
        // Arrange
        when(userService.getUserByUsername("nonexistent"))
                .thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{username}", "nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }

    @Test
    void assignUserToDepartment_NotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("User not found"))
                .when(userService).assignUserToDepartment("nonexistent");

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{username}/department", "nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }

    @Test
    void assignUserToRole_NotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("User not found"))
                .when(userService).assignUserToRole(eq("nonexistent"), any(RoleAssignmentRequest.class));

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{username}/role", "nonexistent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"TEAM_LEADER\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }

    @Test
    void assignUserToProject_NotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("User not found"))
                .when(userService).assignUserToProject(eq("nonexistent"), any(ProjectAssignmentRequest.class));

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{username}/project", "nonexistent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\":\"" + projectId + "\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }

    @Test
    void assignUserToRole_InvalidRole() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{username}/role", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"INVALID_ROLE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void assignUserToProject_InvalidRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{username}/project", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"projectId\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
} 