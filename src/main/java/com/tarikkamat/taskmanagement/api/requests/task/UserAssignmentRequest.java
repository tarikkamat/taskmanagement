package com.tarikkamat.taskmanagement.api.requests.task;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserAssignmentRequest(
        @NotNull(message = "UserId is required")
        UUID userId
) {
}
