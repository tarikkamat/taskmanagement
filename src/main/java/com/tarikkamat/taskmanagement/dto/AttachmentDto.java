package com.tarikkamat.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for {@link com.tarikkamat.taskmanagement.entity.Attachment}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AttachmentDto(
        UUID id,
        String createdBy,
        String updatedBy,
        String deletedBy,
        Date createdAt,
        Date updatedAt,
        Date deletedAt,
        String fileName,
        String filePath
) implements Serializable {
}