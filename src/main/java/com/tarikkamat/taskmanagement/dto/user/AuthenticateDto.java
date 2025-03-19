package com.tarikkamat.taskmanagement.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public record AuthenticateDto(
        @NotBlank(message = "Email or username is required")
        @Size(min = 3, max = 50, message = "Email or username must be between 3 and 50 characters")
        String identifier,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password
) implements Serializable {}
