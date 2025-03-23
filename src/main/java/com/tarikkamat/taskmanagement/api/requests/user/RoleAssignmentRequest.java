package com.tarikkamat.taskmanagement.api.requests.user;

import com.tarikkamat.taskmanagement.enums.Role;
import jakarta.validation.constraints.NotNull;

public record RoleAssignmentRequest(
        @NotNull(message = "Role is required")
        Role role
) {
}
