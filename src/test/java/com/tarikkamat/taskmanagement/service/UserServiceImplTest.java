package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.Department;
import com.tarikkamat.taskmanagement.entity.Project;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.Role;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import com.tarikkamat.taskmanagement.requests.ProjectAssignmentRequest;
import com.tarikkamat.taskmanagement.requests.RoleAssignmentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ProjectService projectService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDto testUserDto;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setId(UUID.randomUUID());
        testDepartment.setName("Test Department");

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(Role.TEAM_MEMBER);
        testUser.setDepartment(testDepartment);
        testUser.setProjects(new ArrayList<>());

        testUserDto = new UserDto(
                testUser.getId(),
                "Test User",
                testUser.getEmail(),
                testUser.getUsername(),
                testUser.getPassword(),
                testUser.getRole()
        );

        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getUserByUsername_ShouldReturnUserDto() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        UserDto result = userService.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(testUserDto.username(), result.username());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void getUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByUsername("nonexistent"));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<User> users = List.of(testUser);
        List<UserDto> userDtos = List.of(testUserDto);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDtoList(users)).thenReturn(userDtos);

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void assignUserToDepartment_ShouldUpdateUserDepartment() {
        User currentUser = new User();
        currentUser.setDepartment(testDepartment);

        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        lenient().when(userMapper.toDto(any(User.class))).thenReturn(testUserDto);
        doReturn(testUser).when(userMapper).toEntity(any());

        userService.assignUserToDepartment("testuser");

        verify(userRepository).save(testUser);
        assertEquals(testDepartment, testUser.getDepartment());
    }

    @Test
    void assignUserToRole_ShouldUpdateUserRole() {
        User currentUser = new User();
        currentUser.setRole(Role.GROUP_MANAGER);
        currentUser.setDepartment(testDepartment);
        testUser.setDepartment(testDepartment);

        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        lenient().when(userMapper.toDto(any(User.class))).thenReturn(testUserDto);
        doReturn(testUser).when(userMapper).toEntity(any());

        RoleAssignmentRequest request = new RoleAssignmentRequest(Role.PROJECT_MANAGER);
        userService.assignUserToRole("testuser", request);

        verify(userRepository).save(testUser);
        assertEquals(Role.PROJECT_MANAGER, testUser.getRole());
    }

    @Test
    void assignUserToProject_ShouldAddProjectToUser() {
        User currentUser = new User();
        currentUser.setDepartment(testDepartment);
        testUser.setDepartment(testDepartment);

        Project project = new Project();
        project.setId(UUID.randomUUID());

        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        lenient().when(userMapper.toDto(any(User.class))).thenReturn(testUserDto);
        doReturn(testUser).when(userMapper).toEntity(any());
        when(projectService.getProjectById(any())).thenReturn(null);
        when(projectService.toEntity(any())).thenReturn(project);

        ProjectAssignmentRequest request = new ProjectAssignmentRequest(UUID.randomUUID());
        userService.assignUserToProject("testuser", request);

        verify(userRepository).save(testUser);
        assertTrue(testUser.getProjects().contains(project));
    }

    @Test
    void findByEmailOrUsername_ShouldReturnUser() {
        when(userRepository.findByEmailOrUsername("test@example.com")).thenReturn(Optional.of(testUser));

        User result = userService.findByEmailOrUsername("test@example.com");

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository).findByEmailOrUsername("test@example.com");
    }

    @Test
    void assignUserToRole_ShouldThrowException_WhenUserNotInSameDepartment() {
        User currentUser = new User();
        currentUser.setRole(Role.GROUP_MANAGER);
        currentUser.setDepartment(new Department());
        testUser.setDepartment(testDepartment);

        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        lenient().when(userMapper.toDto(any(User.class))).thenReturn(testUserDto);
        doReturn(testUser).when(userMapper).toEntity(any());

        RoleAssignmentRequest request = new RoleAssignmentRequest(Role.PROJECT_MANAGER);
        
        assertThrows(RuntimeException.class, () -> userService.assignUserToRole("testuser", request));
    }

    @Test
    void assignUserToRole_ShouldThrowException_WhenNonGroupManagerAssignsGroupManager() {
        User currentUser = new User();
        currentUser.setRole(Role.PROJECT_MANAGER);
        currentUser.setDepartment(testDepartment);
        testUser.setDepartment(testDepartment);

        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        lenient().when(userMapper.toDto(any(User.class))).thenReturn(testUserDto);
        doReturn(testUser).when(userMapper).toEntity(any());

        RoleAssignmentRequest request = new RoleAssignmentRequest(Role.GROUP_MANAGER);
        
        assertThrows(RuntimeException.class, () -> userService.assignUserToRole("testuser", request));
    }

    @Test
    void getAllUsers_ShouldThrowException_WhenNoUsersFound() {
        when(userRepository.findAll()).thenReturn(List.of());
        
        assertThrows(RuntimeException.class, () -> userService.getAllUsers());
    }

    @Test
    void findByEmailOrUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmailOrUsername("nonexistent")).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> userService.findByEmailOrUsername("nonexistent"));
    }

    @Test
    void createUser_ShouldSaveUser() {
        when(userMapper.toEntity(testUserDto)).thenReturn(testUser);
        
        userService.createUser(testUserDto);
        
        verify(userRepository).save(testUser);
    }
} 