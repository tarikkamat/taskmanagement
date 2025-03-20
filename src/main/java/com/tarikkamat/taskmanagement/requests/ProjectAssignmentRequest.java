package com.tarikkamat.taskmanagement.requests;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ProjectAssignmentRequest(
        @NotBlank(message = "projectId is required")
        UUID projectId
) {
}
