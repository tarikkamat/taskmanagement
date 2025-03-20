package com.tarikkamat.taskmanagement.requests;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProjectAssignmentRequest(
        @NotNull(message = "projectId is required")
        UUID projectId
) {
}
