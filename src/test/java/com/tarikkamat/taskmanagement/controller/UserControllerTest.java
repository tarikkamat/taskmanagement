package com.tarikkamat.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.enums.Role;
import com.tarikkamat.taskmanagement.requests.ProjectAssignmentRequest;
import com.tarikkamat.taskmanagement.requests.RoleAssignmentRequest;
import com.tarikkamat.taskmanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUserDto = new UserDto(
                UUID.randomUUID(),
                "Test User",
                "test@example.com",
                "testuser",
                "password",
                Role.TEAM_MEMBER
        );
    }

    @Test
    @WithMockUser
    void getAllUsers_ShouldReturnUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(testUserDto));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data[0].username").value(testUserDto.username()));
    }

    @Test
    @WithMockUser
    void getUserByUsername_ShouldReturnUser() throws Exception {
        when(userService.getUserByUsername("testuser")).thenReturn(testUserDto);

        mockMvc.perform(get("/api/v1/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.username").value(testUserDto.username()));
    }

    @Test
    @WithMockUser(authorities = "GROUP_MANAGER")
    void assignUserToDepartment_ShouldSucceed() throws Exception {
        mockMvc.perform(patch("/api/v1/users/testuser/department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    @WithMockUser(authorities = "GROUP_MANAGER")
    void assignUserToRole_ShouldSucceed() throws Exception {
        RoleAssignmentRequest request = new RoleAssignmentRequest(Role.PROJECT_MANAGER);
        
        Mockito.doNothing().when(userService).assignUserToRole(eq("testuser"), any(RoleAssignmentRequest.class));

        mockMvc.perform(patch("/api/v1/users/testuser/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    @WithMockUser(authorities = "PROJECT_MANAGER")
    void assignUserToProject_ShouldSucceed() throws Exception {
        ProjectAssignmentRequest request = new ProjectAssignmentRequest(UUID.randomUUID());
        
        Mockito.doNothing().when(userService).assignUserToProject(eq("testuser"), any(ProjectAssignmentRequest.class));

        mockMvc.perform(patch("/api/v1/users/testuser/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    @WithMockUser
    void getUserByUsername_ShouldReturn404_WhenUserNotFound() throws Exception {
        when(userService.getUserByUsername("nonexistent"))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/v1/users/nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    @WithMockUser
    void getAllUsers_ShouldReturn404_WhenNoUsersFound() throws Exception {
        when(userService.getAllUsers())
                .thenThrow(new RuntimeException("No users found"));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("No users found"));
    }

    @Test
    @WithMockUser(authorities = "TEAM_MEMBER")
    void assignUserToDepartment_ShouldReturn403_WhenUnauthorized() throws Exception {
        mockMvc.perform(patch("/api/v1/users/testuser/department"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "TEAM_MEMBER")
    void assignUserToRole_ShouldReturn403_WhenUnauthorized() throws Exception {
        RoleAssignmentRequest request = new RoleAssignmentRequest(Role.PROJECT_MANAGER);

        mockMvc.perform(patch("/api/v1/users/testuser/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "TEAM_MEMBER")
    void assignUserToProject_ShouldReturn403_WhenUnauthorized() throws Exception {
        ProjectAssignmentRequest request = new ProjectAssignmentRequest(UUID.randomUUID());

        mockMvc.perform(patch("/api/v1/users/testuser/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "GROUP_MANAGER")
    void assignUserToDepartment_ShouldReturn404_WhenUserNotFound() throws Exception {
        doThrow(new RuntimeException("User not found"))
                .when(userService).assignUserToDepartment("nonexistent");

        mockMvc.perform(patch("/api/v1/users/nonexistent/department"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("User not found"));
    }
} 