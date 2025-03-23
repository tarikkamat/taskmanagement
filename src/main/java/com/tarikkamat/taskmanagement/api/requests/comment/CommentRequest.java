package com.tarikkamat.taskmanagement.api.requests.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

public record CommentRequest(
        @NotNull(message = "TaskId field is required")
        UUID taskId,

        @NotBlank(message = "Comment content field is required")
        @Size(min = 1, max = 500, message = "The comment content must be between 1 and 500 characters")
        String content
) implements Serializable {
} 