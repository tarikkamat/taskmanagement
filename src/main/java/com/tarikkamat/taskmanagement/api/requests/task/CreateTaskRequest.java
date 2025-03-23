package com.tarikkamat.taskmanagement.api.requests.task;

import com.tarikkamat.taskmanagement.enums.Priority;
import com.tarikkamat.taskmanagement.enums.TaskState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

public record CreateTaskRequest(
        @NotBlank(message = "Title field is required")
        @Size(min = 3, max = 100, message = "The title must be between 3 and 100 characters")
        String title,

        @NotBlank(message = "User Story Description field is required")
        @Size(min = 10, max = 1000, message = "The description must be between 10 and 1000 characters")
        String userStoryDescription,
        
        @Size(max = 1000, message = "The acceptance criteria must be at most 1000 characters")
        String acceptanceCriteria,

        @NotNull(message = "Priority field is required")
        Priority priority,
        
        TaskState state,
        
        String stateReason,

        @NotNull(message = "ProjectId field is required")
        UUID projectId,

        UUID assigneeId
) implements Serializable {
} 