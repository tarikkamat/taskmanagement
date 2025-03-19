package com.tarikkamat.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link com.tarikkamat.taskmanagement.entity.Comment}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentDto(
        UUID id,
        String createdBy,
        String updatedBy,
        String deletedBy,
        Date createdAt,
        Date updatedAt,
        Date deletedAt,
        String content
) implements Serializable {
}