package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.enums.ProjectStatus;
import com.tarikkamat.taskmanagement.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private MockMvc mockMvc;
    private UUID projectId;
    private Date now;
    private ProjectDto projectDto;
    private List<ProjectDto> projectList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectController)
                .build();
        projectId = UUID.randomUUID();
        now = new Date();

        projectDto = new ProjectDto(
                projectId,
                "testUser",
                "testUser",
                null,
                now,
                now,
                null,
                "Test Project",
                "Test Project Description",
                "Test Department",
                ProjectStatus.IN_PROGRESS,
                new ArrayList<>()
        );

        projectList = new ArrayList<>();
        projectList.add(projectDto);
    }

    @Test
    void getAllProjects_Success() throws Exception {
        when(projectService.getAllProjects()).thenReturn(projectList);

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Projects found"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data[0].title").value("Test Project"))
                .andExpect(jsonPath("$.data[0].description").value("Test Project Description"));
    }

    @Test
    void getAllProjects_NotFound() throws Exception {
        when(projectService.getAllProjects())
                .thenThrow(new RuntimeException("No projects found"));

        mockMvc.perform(get("/api/v1/projects"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("No projects found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }

    @Test
    void getProjectById_Success() throws Exception {
        when(projectService.getProjectById(projectId)).thenReturn(projectDto);

        mockMvc.perform(get("/api/v1/projects/{projectId}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Project found"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.title").value("Test Project"))
                .andExpect(jsonPath("$.data.description").value("Test Project Description"));
    }

    @Test
    void getProjectById_NotFound() throws Exception {
        when(projectService.getProjectById(projectId))
                .thenThrow(new RuntimeException("Project not found"));

        mockMvc.perform(get("/api/v1/projects/{projectId}", projectId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Project not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }

    @Test
    void updateProjectStatus_Success() throws Exception {
        when(projectService.updateProjectStatus(any(), any())).thenReturn(projectDto);

        mockMvc.perform(patch("/api/v1/projects/{id}/status", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"COMPLETED\",\"reason\":\"Project completed successfully\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Project state updated"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.title").value("Test Project"));
    }

    @Test
    void updateProjectStatus_InvalidStatus() throws Exception {
        when(projectService.updateProjectStatus(any(), any()))
                .thenThrow(new RuntimeException("Invalid project status"));

        mockMvc.perform(patch("/api/v1/projects/{id}/status", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INVALID_STATUS\",\"reason\":\"Invalid status provided\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Invalid project status"))
                .andExpect(jsonPath("$.httpStatusCode").value(400));
    }

    @Test
    void updateProjectStatus_Unauthorized() throws Exception {
        when(projectService.updateProjectStatus(any(), any()))
                .thenThrow(new RuntimeException("Unauthorized access"));

        mockMvc.perform(patch("/api/v1/projects/{id}/status", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"COMPLETED\",\"reason\":\"Project completed successfully\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Unauthorized access"))
                .andExpect(jsonPath("$.httpStatusCode").value(400));
    }

    @Test
    void updateProjectStatus_InvalidReason() throws Exception {
        when(projectService.updateProjectStatus(any(), any()))
                .thenThrow(new RuntimeException("Invalid reason: too short"));

        mockMvc.perform(patch("/api/v1/projects/{id}/status", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"COMPLETED\",\"reason\":\"too short\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Invalid reason: too short"))
                .andExpect(jsonPath("$.httpStatusCode").value(400));
    }
} 