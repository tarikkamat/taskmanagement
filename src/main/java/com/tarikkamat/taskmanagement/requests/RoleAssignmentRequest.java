package com.tarikkamat.taskmanagement.requests;

import com.tarikkamat.taskmanagement.enums.Role;
import jakarta.validation.constraints.NotNull;

public record RoleAssignmentRequest(
        @NotNull(message = "Role is required")
        Role role
) {
}
