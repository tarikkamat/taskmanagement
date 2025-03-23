package com.tarikkamat.taskmanagement.api.requests.task;

import com.tarikkamat.taskmanagement.enums.TaskState;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UpdateTaskStatusRequest(
        @NotNull(message = "Status field is required")
        TaskState status,

        @Size(min = 10, max = 500, message = "Reason field is required and must be between 10 and 500 characters")
        String reason
) implements Serializable {
} 