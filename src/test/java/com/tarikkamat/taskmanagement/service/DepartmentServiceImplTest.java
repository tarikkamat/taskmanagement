package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.DepartmentDto;
import com.tarikkamat.taskmanagement.entity.Department;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.mapper.DepartmentMapper;
import com.tarikkamat.taskmanagement.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private User mockUser;
    private Department mockDepartment;
    private DepartmentDto mockDepartmentDto;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setUsername("testUser");

        mockDepartment = new Department();
        mockDepartment.setId(UUID.randomUUID());
        mockDepartment.setName("Test Department");
        mockDepartment.setManager(mockUser);

        mockDepartmentDto = new DepartmentDto(
            mockDepartment.getId(),
            mockUser.getUsername(),
            null,
            null,
            new Date(),
            null,
            null,
            "Test Department",
            "Test Description",
            List.of(),
            List.of()
        );

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
    }

    @Test
    void getDepartment_Success() {
        // Arrange
        when(departmentRepository.findByManager_Id(mockUser.getId())).thenReturn(mockDepartment);
        when(departmentMapper.toDto(mockDepartment)).thenReturn(mockDepartmentDto);

        // Act
        DepartmentDto result = departmentService.getDepartment();

        // Assert
        assertNotNull(result);
        assertEquals(mockDepartmentDto.name(), result.name());
        assertEquals(mockDepartmentDto.description(), result.description());
    }

    @Test
    void getDepartment_NotFound() {
        // Arrange
        when(departmentRepository.findByManager_Id(mockUser.getId())).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> departmentService.getDepartment());
    }
} 