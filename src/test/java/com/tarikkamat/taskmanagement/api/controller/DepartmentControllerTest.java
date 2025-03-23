package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.DepartmentDto;
import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private MockMvc mockMvc;
    private UUID departmentId;
    private Date now;
    private DepartmentDto departmentDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
        departmentId = UUID.randomUUID();
        now = new Date();

        // Test verilerini oluştur
        departmentDto = new DepartmentDto(
                departmentId,
                "testUser",
                "testUser",
                null,
                now,
                now,
                null,
                "Test Department",
                "Test Department Description",
                new ArrayList<ProjectDto>(),
                new ArrayList<UserDto>()
        );
    }

    @Test
    void getDepartment_Success() throws Exception {
        // Arrange
        when(departmentService.getDepartment()).thenReturn(departmentDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Department found"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.name").value("Test Department"))
                .andExpect(jsonPath("$.data.description").value("Test Department Description"));
    }

    @Test
    void getDepartment_NotFound() throws Exception {
        // Arrange
        when(departmentService.getDepartment())
                .thenThrow(new RuntimeException("Department not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Department not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }
} 