package com.tarikkamat.taskmanagement.api.requests.attachment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttachmentRequest {
    @NotNull(message = "File is required")
    private MultipartFile file;

    @NotNull(message = "TaskID is required")
    private Long taskId;
} 