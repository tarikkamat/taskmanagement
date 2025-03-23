package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.AttachmentDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AttachmentService {
    AttachmentDto uploadFile(UUID taskId, MultipartFile file) throws IOException;

    AttachmentDto getFile(UUID id);

    void deleteFile(UUID id);

    List<AttachmentDto> getFilesByTaskId(UUID taskId);
}
