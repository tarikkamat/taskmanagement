package com.tarikkamat.taskmanagement.api.requests.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProjectAssignmentRequest(
        @NotNull(message = "ProjectId is required")
        UUID projectId
) {
}
