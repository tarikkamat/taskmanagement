package com.tarikkamat.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tarikkamat.taskmanagement.enums.Priority;
import com.tarikkamat.taskmanagement.enums.TaskState;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link com.tarikkamat.taskmanagement.entity.Task}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TaskDto(
        UUID id,
        String createdBy,
        String updatedBy,
        String deletedBy,
        Date createdAt,
        Date updatedAt,
        Date deletedAt,
        String title,
        String userStoryDescription,
        String acceptanceCriteria,
        TaskState state,
        Priority priority,
        String stateReason,
        Integer progressPercentage
) implements Serializable {
}