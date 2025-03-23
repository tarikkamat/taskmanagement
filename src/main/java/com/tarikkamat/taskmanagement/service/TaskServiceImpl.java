package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.task.CreateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UpdateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UserAssignmentRequest;
import com.tarikkamat.taskmanagement.dto.TaskDto;
import com.tarikkamat.taskmanagement.entity.Task;
import com.tarikkamat.taskmanagement.enums.Priority;
import com.tarikkamat.taskmanagement.enums.TaskState;
import com.tarikkamat.taskmanagement.mapper.TaskMapper;
import com.tarikkamat.taskmanagement.repository.ProjectRepository;
import com.tarikkamat.taskmanagement.repository.TaskRepository;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskDto createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setUserStoryDescription(request.userStoryDescription());
        task.setAcceptanceCriteria(request.acceptanceCriteria());
        task.setPriority(request.priority());
        task.setState(request.state() != null ? request.state() : TaskState.BACKLOG);
        task.setStateReason(request.stateReason());

        if (request.projectId() != null) {
            projectRepository.findById(request.projectId())
                    .ifPresent(task::setProject);
        }

        if (request.assigneeId() != null) {
            userRepository.findById(request.assigneeId())
                    .ifPresent(task::setAssignee);
        }

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDto getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, not found taskId: " + id));
        return taskMapper.toDto(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getAllTasks() {
        return taskRepository.findByDeletedAtIsNull().stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByProjectId(UUID projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByAssigneeId(UUID assigneeId) {
        return taskRepository.findByAssigneeId(assigneeId).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByState(TaskState state) {
        return taskRepository.findByState(state).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByPriority(String priority) {
        try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            return taskRepository.findByPriority(priorityEnum).stream()
                    .map(taskMapper::toDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid priority value: " + priority);
        }
    }

    @Override
    @Transactional
    public TaskDto updateTask(UUID id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, not found taskId: " + id));

        if (request.title() != null) {
            task.setTitle(request.title());
        }

        if (request.userStoryDescription() != null) {
            task.setUserStoryDescription(request.userStoryDescription());
        }

        if (request.acceptanceCriteria() != null) {
            task.setAcceptanceCriteria(request.acceptanceCriteria());
        }

        if (request.priority() != null) {
            task.setPriority(request.priority());
        }

        if (request.assigneeId() != null) {
            userRepository.findById(request.assigneeId())
                    .ifPresent(task::setAssignee);
        }

        if (request.state() != null && !request.state().equals(task.getState())) {
            validateStateTransition(task.getState(), request.state(), request.stateReason());
            task.setState(request.state());
            task.setStateReason(request.stateReason());
        }

        task.setUpdatedAt(new Date());
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto updateTaskState(UUID id, TaskState newState, String stateReason) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, not found taskId: " + id));

        validateStateTransition(task.getState(), newState, stateReason);

        task.setState(newState);
        task.setStateReason(stateReason);
        task.setUpdatedAt(new Date());

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, not found taskId: " + id));

        task.setDeletedAt(new Date());
        taskRepository.save(task);
    }

    /**
     * Happy Path: Backlog ↔ In Analysis ↔ In Development/Progress ↔ Completed
     * Cancel Path: Herhangi bir durumdan (Completed hariç) → Cancelled
     * Blocked Paths: In Analysis ↔ Blocked, In Development/Progress ↔ Blocked
     */
    private void validateStateTransition(TaskState currentState, TaskState newState, String stateReason) {
        // Completed durumundan başka bir duruma geçiş kontrolü
        if (currentState == TaskState.COMPLETED) {
            throw new RuntimeException("A completed task cannot be changed to any other state");
        }

        // Cancelled veya Blocked durumuna geçişte reason kontrolü
        if ((newState == TaskState.CANCELLED || newState == TaskState.BLOCKED) && (stateReason == null || stateReason.trim().isEmpty())) {
            throw new RuntimeException("State reason is required when changing to Cancelled or Blocked state");
        }

        // Happy Path kontrolü
        if (newState == TaskState.COMPLETED) {
            if (currentState != TaskState.IN_PROGRESS) {
                throw new RuntimeException("Task can only be completed from In Progress state");
            }
        }

        // Blocked durumuna geçiş kontrolü
        if (newState == TaskState.BLOCKED) {
            if (currentState != TaskState.IN_ANALYSIS && currentState != TaskState.IN_PROGRESS) {
                throw new RuntimeException("Task can only be blocked from In Analysis or In Progress state");
            }
        }

        // Durumlar arası geçiş mantığı
        if (newState == TaskState.IN_ANALYSIS && currentState != TaskState.BACKLOG && currentState != TaskState.BLOCKED) {
            throw new RuntimeException("Task can only move to In Analysis from Backlog or Blocked state");
        }

        if (newState == TaskState.IN_PROGRESS && currentState != TaskState.IN_ANALYSIS && currentState != TaskState.BLOCKED) {
            throw new RuntimeException("Task can only move to In Progress from In Analysis or Blocked state");
        }
    }

    @Override
    @Transactional
    public TaskDto assignTaskToUser(UUID id, UserAssignmentRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error, not found taskId: " + id));

        userRepository.findById(request.userId())
                .ifPresent(task::setAssignee);

        task.setUpdatedAt(new Date());
        return taskMapper.toDto(taskRepository.save(task));
    }
}
