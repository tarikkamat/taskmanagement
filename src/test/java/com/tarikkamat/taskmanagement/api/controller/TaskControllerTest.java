package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.api.requests.task.CreateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UpdateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UpdateTaskStatusRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UserAssignmentRequest;
import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.TaskDto;
import com.tarikkamat.taskmanagement.enums.Priority;
import com.tarikkamat.taskmanagement.enums.TaskState;
import com.tarikkamat.taskmanagement.service.TaskService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;
    private UUID taskId;
    private UUID projectId;
    private UUID assigneeId;
    private Date now;
    private TaskDto taskDto;
    private CreateTaskRequest createTaskRequest;
    private UpdateTaskRequest updateTaskRequest;
    private UpdateTaskStatusRequest updateTaskStatusRequest;
    private UserAssignmentRequest userAssignmentRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        taskId = UUID.randomUUID();
        projectId = UUID.randomUUID();
        assigneeId = UUID.randomUUID();
        now = new Date();

        // Test verilerini oluştur
        taskDto = new TaskDto(
                taskId,
                "testUser",
                "testUser",
                null,
                now,
                now,
                null,
                "Test Task",
                "Test Task Description",
                "Test Acceptance Criteria",
                TaskState.BACKLOG,
                Priority.HIGH,
                null,
                0
        );

        createTaskRequest = new CreateTaskRequest(
                "Test Task",
                "Test Task Description",
                "Test Acceptance Criteria",
                Priority.HIGH,
                TaskState.BACKLOG,
                null,
                projectId,
                assigneeId
        );

        updateTaskRequest = new UpdateTaskRequest(
                "Updated Task",
                "Updated Task Description",
                "Updated Acceptance Criteria",
                Priority.MEDIUM,
                TaskState.IN_PROGRESS,
                "Task started",
                assigneeId
        );

        updateTaskStatusRequest = new UpdateTaskStatusRequest(
                TaskState.IN_PROGRESS,
                "Task started"
        );

        userAssignmentRequest = new UserAssignmentRequest(assigneeId);
    }

    @Test
    void createTask_Success() throws Exception {
        // Arrange
        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Task\",\"userStoryDescription\":\"Test Task Description\",\"acceptanceCriteria\":\"Test Acceptance Criteria\",\"priority\":\"HIGH\",\"state\":\"BACKLOG\",\"projectId\":\"" + projectId + "\",\"assigneeId\":\"" + assigneeId + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Task created"))
                .andExpect(jsonPath("$.httpStatusCode").value(201))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void getTaskById_Success() throws Exception {
        // Arrange
        when(taskService.getTaskById(taskId)).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Task found"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void getAllTasks_Success() throws Exception {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(taskDto);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("All tasks retrieved"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Task"));
    }

    @Test
    void getTasksByProjectId_Success() throws Exception {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(taskDto);
        when(taskService.getTasksByProjectId(projectId)).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/project/{projectId}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Project tasks retrieved"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Task"));
    }

    @Test
    void getTasksByAssigneeId_Success() throws Exception {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(taskDto);
        when(taskService.getTasksByAssigneeId(assigneeId)).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/assignee/{assigneeId}", assigneeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Assignee tasks retrieved"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Task"));
    }

    @Test
    void getTasksByState_Success() throws Exception {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(taskDto);
        when(taskService.getTasksByState(TaskState.BACKLOG)).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/state/BACKLOG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("State tasks retrieved"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Task"));
    }

    @Test
    void getTasksByPriority_Success() throws Exception {
        // Arrange
        List<TaskDto> tasks = Arrays.asList(taskDto);
        when(taskService.getTasksByPriority("HIGH")).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/priority/HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Priority tasks retrieved"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Task"));
    }

    @Test
    void updateTask_Success() throws Exception {
        // Arrange
        when(taskService.updateTask(any(UUID.class), any(UpdateTaskRequest.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Task\",\"userStoryDescription\":\"Updated Task Description\",\"acceptanceCriteria\":\"Updated Acceptance Criteria\",\"priority\":\"MEDIUM\",\"state\":\"IN_PROGRESS\",\"stateReason\":\"Task started\",\"assigneeId\":\"" + assigneeId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Task updated"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void updateTaskState_Success() throws Exception {
        // Arrange
        when(taskService.updateTaskState(any(UUID.class), any(TaskState.class), any(String.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/tasks/{id}/state", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\",\"reason\":\"Task started\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Task state updated"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void assignTaskToUser_Success() throws Exception {
        // Arrange
        when(taskService.assignTaskToUser(any(UUID.class), any(UserAssignmentRequest.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/tasks/{id}/assignee", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + assigneeId + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Task assignee updated"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void deleteTask_Success() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask(taskId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Task deleted"))
                .andExpect(jsonPath("$.httpStatusCode").value(200));

        verify(taskService).deleteTask(taskId);
    }

    @Test
    void createTask_InvalidRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"userStoryDescription\":\"\",\"acceptanceCriteria\":\"\",\"priority\":\"\",\"state\":\"\",\"projectId\":\"" + projectId + "\",\"assigneeId\":\"" + assigneeId + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTaskById_NotFound() throws Exception {
        // Arrange
        when(taskService.getTaskById(taskId))
                .thenThrow(new RuntimeException("Task not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }

    @Test
    void updateTaskState_InvalidState() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/v1/tasks/{id}/state", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INVALID_STATE\",\"reason\":\"Invalid state provided\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTask_NotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Task not found"))
                .when(taskService).deleteTask(taskId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(400));
    }
} 