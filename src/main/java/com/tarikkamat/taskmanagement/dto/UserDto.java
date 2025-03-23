package com.tarikkamat.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tarikkamat.taskmanagement.enums.Role;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.tarikkamat.taskmanagement.entity.User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDto(
        UUID id,
        String fullName,
        String email,
        String username,
        String password,
        Role role
) implements Serializable {
}