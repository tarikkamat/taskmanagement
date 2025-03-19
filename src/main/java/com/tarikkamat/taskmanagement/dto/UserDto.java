package com.tarikkamat.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tarikkamat.taskmanagement.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.tarikkamat.taskmanagement.entity.User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDto(
        UUID id,
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        Role role
) implements Serializable {
}