package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.task.CreateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UpdateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UserAssignmentRequest;
import com.tarikkamat.taskmanagement.dto.TaskDto;
import com.tarikkamat.taskmanagement.enums.TaskState;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskDto createTask(CreateTaskRequest request);

    TaskDto getTaskById(UUID id);

    List<TaskDto> getAllTasks();

    List<TaskDto> getTasksByProjectId(UUID projectId);

    List<TaskDto> getTasksByAssigneeId(UUID assigneeId);

    List<TaskDto> getTasksByState(TaskState state);

    List<TaskDto> getTasksByPriority(String priority);

    TaskDto updateTask(UUID id, UpdateTaskRequest request);

    TaskDto updateTaskState(UUID id, TaskState newState, String stateReason);

    TaskDto assignTaskToUser(UUID id, UserAssignmentRequest request);

    void deleteTask(UUID id);
}
