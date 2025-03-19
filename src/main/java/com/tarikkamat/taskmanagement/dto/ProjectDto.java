package com.tarikkamat.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tarikkamat.taskmanagement.enums.ProjectStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link com.tarikkamat.taskmanagement.entity.Project}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProjectDto(
        UUID id,
        String createdBy,
        String updatedBy,
        String deletedBy,
        Date createdAt,
        Date updatedAt,
        Date deletedAt,
        String title,
        String description,
        String departmentName,
        ProjectStatus status
) implements Serializable {
}