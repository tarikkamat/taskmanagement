package com.tarikkamat.taskmanagement.requests;

import com.tarikkamat.taskmanagement.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record RoleAssignmentRequest(
        @NotBlank(message = "Role is required")
        Role role
) {
}
