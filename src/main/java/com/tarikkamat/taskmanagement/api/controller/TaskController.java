package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.api.requests.task.CreateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UpdateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UpdateTaskStatusRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UserAssignmentRequest;
import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.TaskDto;
import com.tarikkamat.taskmanagement.enums.TaskState;
import com.tarikkamat.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/tasks")
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER', 'PROJECT_MANAGER', 'TEAM_LEADER')")
    public ResponseEntity<BaseResponse<TaskDto>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        try {
            TaskDto taskDto = taskService.createTask(request);
            return ResponseEntity.status(201).body(new BaseResponse<>(true, "Task created", 201, taskDto));
        } catch (Exception e) {
            log.error("Task not created. Error: {}", e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<BaseResponse<TaskDto>> getTaskById(@PathVariable UUID id) {
        try {
            TaskDto taskDto = taskService.getTaskById(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task found", 200, taskDto));
        } catch (Exception e) {
            log.error("Task not found. Error: {}", e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<BaseResponse<List<TaskDto>>> getAllTasks() {
        try {
            List<TaskDto> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(new BaseResponse<>(true, "All tasks retrieved", 200, tasks));
        } catch (Exception e) {
            log.error("Tasks not retrieved. Error: {}", e.getMessage());
            return ResponseEntity.status(500).body(new BaseResponse<>(false, e.getMessage(), 500, null));
        }
    }

    @GetMapping("/tasks/project/{projectId}")
    public ResponseEntity<BaseResponse<List<TaskDto>>> getTasksByProjectId(@PathVariable UUID projectId) {
        try {
            List<TaskDto> tasks = taskService.getTasksByProjectId(projectId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Project tasks retrieved", 200, tasks));
        } catch (Exception e) {
            log.error("Project tasks not retrieved. Error: {}", e.getMessage());
            return ResponseEntity.status(500).body(new BaseResponse<>(false, e.getMessage(), 500, null));
        }
    }

    @GetMapping("/tasks/assignee/{assigneeId}")
    public ResponseEntity<BaseResponse<List<TaskDto>>> getTasksByAssigneeId(@PathVariable UUID assigneeId) {
        try {
            List<TaskDto> tasks = taskService.getTasksByAssigneeId(assigneeId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Assignee tasks retrieved", 200, tasks));
        } catch (Exception e) {
            log.error("Assignee tasks not retrieved. Error: {}", e.getMessage());
            return ResponseEntity.status(500).body(new BaseResponse<>(false, e.getMessage(), 500, null));
        }
    }

    @GetMapping("/tasks/state/{state}")
    public ResponseEntity<BaseResponse<List<TaskDto>>> getTasksByState(@PathVariable String state) {
        try {
            TaskState taskState = TaskState.valueOf(state.toUpperCase());
            List<TaskDto> tasks = taskService.getTasksByState(taskState);
            return ResponseEntity.ok(new BaseResponse<>(true, "State tasks retrieved", 200, tasks));
        } catch (IllegalArgumentException e) {
            log.error("Invalid state value: {}", state);
            return ResponseEntity.status(400).body(new BaseResponse<>(false, "Invalid state value: " + state, 400, null));
        } catch (Exception e) {
            log.error("State tasks not retrieved. Error: {}", e.getMessage());
            return ResponseEntity.status(500).body(new BaseResponse<>(false, e.getMessage(), 500, null));
        }
    }

    @GetMapping("/tasks/priority/{priority}")
    public ResponseEntity<BaseResponse<List<TaskDto>>> getTasksByPriority(@PathVariable String priority) {
        try {
            List<TaskDto> tasks = taskService.getTasksByPriority(priority);
            return ResponseEntity.ok(new BaseResponse<>(true, "Priority tasks retrieved", 200, tasks));
        } catch (Exception e) {
            log.error("Priority tasks not retrieved. Error: {}", e.getMessage());
            return ResponseEntity.status(500).body(new BaseResponse<>(false, e.getMessage(), 500, null));
        }
    }

    @PatchMapping("/tasks/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER', 'PROJECT_MANAGER', 'TEAM_LEADER')")
    public ResponseEntity<BaseResponse<TaskDto>> updateTask(@PathVariable UUID id, @Valid @RequestBody UpdateTaskRequest request) {
        try {
            TaskDto taskDto = taskService.updateTask(id, request);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task updated", 200, taskDto));
        } catch (Exception e) {
            log.error("Task not updated. Error: {}", e.getMessage());
            return ResponseEntity.status(400).body(new BaseResponse<>(false, e.getMessage(), 400, null));
        }
    }

    @PatchMapping("/tasks/{id}/state")
    public ResponseEntity<BaseResponse<TaskDto>> updateTaskState(@PathVariable UUID id, @Valid @RequestBody UpdateTaskStatusRequest request) {
        try {
            TaskDto taskDto = taskService.updateTaskState(id, request.status(), request.reason());
            return ResponseEntity.ok(new BaseResponse<>(true, "Task state updated", 200, taskDto));
        } catch (Exception e) {
            log.error("Task state not updated. Error: {}", e.getMessage());
            return ResponseEntity.status(400).body(new BaseResponse<>(false, e.getMessage(), 400, null));
        }
    }

    @PatchMapping("/tasks/{id}/assignee")
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER', 'PROJECT_MANAGER', 'TEAM_LEADER')")
    public ResponseEntity<BaseResponse<TaskDto>> assignTaskToUser(@PathVariable UUID id, @Valid @RequestBody UserAssignmentRequest request) {
        try {
            TaskDto taskDto = taskService.assignTaskToUser(id, request);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task assignee updated", 200, taskDto));
        } catch (Exception e) {
            log.error("Task assignee not updated. Error: {}", e.getMessage());
            return ResponseEntity.status(400).body(new BaseResponse<>(false, e.getMessage(), 400, null));
        }
    }

    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER', 'PROJECT_MANAGER')")
    public ResponseEntity<BaseResponse<Void>> deleteTask(@PathVariable UUID id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task deleted", 200, null));
        } catch (Exception e) {
            log.error("Task not deleted. Error: {}", e.getMessage());
            return ResponseEntity.status(400).body(new BaseResponse<>(false, e.getMessage(), 400, null));
        }
    }
}
