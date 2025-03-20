package com.tarikkamat.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.tarikkamat.taskmanagement.entity.Department}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DepartmentDto(
        UUID id,
        String createdBy,
        String updatedBy,
        String deletedBy,
        Date createdAt,
        Date updatedAt,
        Date deletedAt,
        String name,
        String description,
        List<ProjectDto> projects,
        List<UserDto> members
) implements Serializable {
}