package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.user.ProjectAssignmentRequest;
import com.tarikkamat.taskmanagement.api.requests.user.RoleAssignmentRequest;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.entity.Department;
import com.tarikkamat.taskmanagement.entity.Project;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.Role;
import com.tarikkamat.taskmanagement.enums.ProjectStatus;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;
    private UserDto mockUserDto;
    private Department mockDepartment;
    private Project mockProject;
    private UUID userId;
    private UUID projectId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        projectId = UUID.randomUUID();

        mockDepartment = new Department();
        mockDepartment.setId(UUID.randomUUID());
        mockDepartment.setName("Test Department");

        mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testUser");
        mockUser.setEmail("test@example.com");
        mockUser.setDepartment(mockDepartment);
        mockUser.setRole(Role.TEAM_MEMBER);
        mockUser.setProjects(new ArrayList<>());

        mockUserDto = new UserDto(
            userId,
            "Test User",
            "test@example.com",
            "testUser",
            null,
            Role.TEAM_MEMBER
        );

        mockProject = new Project();
        mockProject.setId(projectId);
        mockProject.setTitle("Test Project");
    }

    @Test
    void getUserByUsername_Success() {
        // Arrange
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);

        // Act
        UserDto result = userService.getUserByUsername("testUser");

        // Assert
        assertNotNull(result);
        assertEquals(mockUserDto.username(), result.username());
        assertEquals(mockUserDto.email(), result.email());
        assertEquals(mockUserDto.role(), result.role());
    }

    @Test
    void createUser_Success() {
        // Arrange
        when(userMapper.toEntity(mockUserDto)).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        userService.createUser(mockUserDto);

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        when(userMapper.toDtoList(List.of(mockUser))).thenReturn(List.of(mockUserDto));

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mockUserDto.username(), result.get(0).username());
    }

    @Test
    void assignUserToDepartment_Success() {
        // Arrange
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);
        when(userMapper.toEntity(mockUserDto)).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        userService.assignUserToDepartment("testUser");

        // Assert
        verify(userRepository).save(any(User.class));
        assertEquals(mockDepartment, mockUser.getDepartment());
    }

    @Test
    void assignUserToRole_Success() {
        // Arrange
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        mockUser.setRole(Role.GROUP_MANAGER);
        RoleAssignmentRequest request = new RoleAssignmentRequest(Role.TEAM_MEMBER);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);
        when(userMapper.toEntity(mockUserDto)).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        userService.assignUserToRole("testUser", request);

        // Assert
        verify(userRepository).save(any(User.class));
        assertEquals(Role.TEAM_MEMBER, mockUser.getRole());
    }

    @Test
    void assignUserToProject_Success() {
        // Arrange
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        ProjectAssignmentRequest request = new ProjectAssignmentRequest(projectId);
        ProjectDto mockProjectDto = new ProjectDto(
            projectId,
            mockUser.getUsername(),
            null,
            null,
            new Date(),
            null,
            null,
            "Test Project",
            "Test Description",
            "Test Department",
            ProjectStatus.IN_PROGRESS,
            List.of()
        );
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);
        when(userMapper.toEntity(mockUserDto)).thenReturn(mockUser);
        when(projectService.getProjectById(projectId)).thenReturn(mockProjectDto);
        when(projectService.toEntity(mockProjectDto)).thenReturn(mockProject);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        userService.assignUserToProject("testUser", request);

        // Assert
        verify(userRepository).save(any(User.class));
        assertTrue(mockUser.getProjects().contains(mockProject));
    }

    @Test
    void findByEmailOrUsername_Success() {
        // Arrange
        when(userRepository.findByEmailOrUsername("testUser")).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.findByEmailOrUsername("testUser");

        // Assert
        assertNotNull(result);
        assertEquals(mockUser.getUsername(), result.getUsername());
        assertEquals(mockUser.getEmail(), result.getEmail());
    }

    @Test
    void toDto_Success() {
        // Arrange
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);

        // Act
        UserDto result = userService.toDto(mockUser);

        // Assert
        assertNotNull(result);
        assertEquals(mockUserDto.username(), result.username());
        assertEquals(mockUserDto.email(), result.email());
        assertEquals(mockUserDto.role(), result.role());
    }
} 