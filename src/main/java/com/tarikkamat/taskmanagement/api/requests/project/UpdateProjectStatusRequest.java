package com.tarikkamat.taskmanagement.api.requests.project;

import com.tarikkamat.taskmanagement.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProjectStatusRequest(
        @NotNull(message = "Status field is required")
        ProjectStatus status,

        @Size(min = 10, max = 500, message = "Reason field is required and must be between 10 and 500 characters")
        String reason
) {
}
