package com.tarikkamat.taskmanagement.api.requests.task;

import com.tarikkamat.taskmanagement.enums.Priority;
import com.tarikkamat.taskmanagement.enums.TaskState;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

public record UpdateTaskRequest(
        @Size(min = 3, max = 100, message = "Title field is required and must be between 3 and 100 characters")
        String title,

        @Size(min = 10, max = 1000, message = "User Story Description field is required and must be between 10 and 1000 characters")
        String userStoryDescription,
        
        @Size(max = 1000, message = "The acceptance criteria must be at most 1000 characters")
        String acceptanceCriteria,

        Priority priority,
        
        TaskState state,
        
        String stateReason,

        UUID assigneeId
) implements Serializable {
} 