package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.task.CreateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UpdateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UserAssignmentRequest;
import com.tarikkamat.taskmanagement.dto.TaskDto;
import com.tarikkamat.taskmanagement.entity.Project;
import com.tarikkamat.taskmanagement.entity.Task;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.Priority;
import com.tarikkamat.taskmanagement.enums.TaskState;
import com.tarikkamat.taskmanagement.mapper.TaskMapper;
import com.tarikkamat.taskmanagement.repository.ProjectRepository;
import com.tarikkamat.taskmanagement.repository.TaskRepository;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User mockUser;
    private Project mockProject;
    private Task mockTask;
    private TaskDto mockTaskDto;
    private UUID taskId;
    private UUID projectId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        projectId = UUID.randomUUID();
        userId = UUID.randomUUID();

        mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testUser");

        mockProject = new Project();
        mockProject.setId(projectId);
        mockProject.setTitle("Test Project");

        mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setTitle("Test Task");
        mockTask.setUserStoryDescription("Test Description");
        mockTask.setAcceptanceCriteria("Test Criteria");
        mockTask.setPriority(Priority.MEDIUM);
        mockTask.setState(TaskState.BACKLOG);
        mockTask.setProject(mockProject);
        mockTask.setAssignee(mockUser);
        mockTask.setCreatedAt(new Date());

        mockTaskDto = new TaskDto(
            taskId,
            mockUser.getUsername(),
            null,
            null,
            new Date(),
            null,
            null,
            "Test Task",
            "Test Description",
            "Test Criteria",
            TaskState.BACKLOG,
            Priority.MEDIUM,
            null,
            0
        );
    }

    @Test
    void createTask_Success() {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest(
            "Test Task",
            "Test Description",
            "Test Criteria",
            Priority.MEDIUM,
            TaskState.BACKLOG,
            null,
            projectId,
            userId
        );
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockProject));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        // Act
        TaskDto result = taskService.createTask(request);

        // Assert
        assertNotNull(result);
        assertEquals(mockTaskDto.title(), result.title());
        assertEquals(mockTaskDto.priority(), result.priority());
        assertEquals(mockTaskDto.state(), result.state());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getTaskById_Success() {
        // Arrange
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        // Act
        TaskDto result = taskService.getTaskById(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(mockTaskDto.title(), result.title());
        assertEquals(mockTaskDto.priority(), result.priority());
        assertEquals(mockTaskDto.state(), result.state());
    }

    @Test
    void getAllTasks_Success() {
        // Arrange
        when(taskRepository.findByDeletedAtIsNull()).thenReturn(List.of(mockTask));
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        // Act
        List<TaskDto> result = taskService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mockTaskDto.title(), result.get(0).title());
    }

    @Test
    void updateTask_Success() {
        // Arrange
        UpdateTaskRequest request = new UpdateTaskRequest(
            "Updated Task",
            "Updated Description",
            "Updated Criteria",
            Priority.HIGH,
            TaskState.IN_ANALYSIS,
            "State reason",
            userId
        );
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        // Act
        TaskDto result = taskService.updateTask(taskId, request);

        // Assert
        assertNotNull(result);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void deleteTask_Success() {
        // Arrange
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository).save(any(Task.class));
        assertNotNull(mockTask.getDeletedAt());
    }

    @Test
    void assignTaskToUser_Success() {
        // Arrange
        UserAssignmentRequest request = new UserAssignmentRequest(userId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        // Act
        TaskDto result = taskService.assignTaskToUser(taskId, request);

        // Assert
        assertNotNull(result);
        verify(taskRepository).save(any(Task.class));
        assertEquals(mockUser, mockTask.getAssignee());
    }
} 