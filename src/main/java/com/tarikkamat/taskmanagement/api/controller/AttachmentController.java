package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.AttachmentDto;
import com.tarikkamat.taskmanagement.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping("/attachments/upload/{taskId}")
    public ResponseEntity<BaseResponse<AttachmentDto>> uploadFile(
            @PathVariable UUID taskId,
            @RequestParam("file") MultipartFile file) {
        BaseResponse<AttachmentDto> response = new BaseResponse<>();
        try {
            AttachmentDto attachmentDto = attachmentService.uploadFile(taskId, file);
            return ResponseEntity.status(201).body(new BaseResponse<>(true, "Attachment uploaded", 201, attachmentDto));
        } catch (Exception e) {
            log.error("Task not created. Error: {}", e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @GetMapping("/attachments/{id}")
    public ResponseEntity<BaseResponse<AttachmentDto>> downloadFile(@PathVariable UUID id) {
        try {
            AttachmentDto attachmentDto = attachmentService.getFile(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Attachment retrieved", 200, attachmentDto));
        } catch (Exception e) {
            log.error("Attachment not found. Error: {}", e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @DeleteMapping("/attachments/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteFile(@PathVariable UUID id) {
        try {
            attachmentService.deleteFile(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Attachment deleted", 200, null));
        } catch (Exception e) {
            log.error("Attachment not deleted. Error: {}", e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @GetMapping("/attachments/task/{taskId}")
    public ResponseEntity<BaseResponse<List<AttachmentDto>>> getFilesByTaskId(@PathVariable UUID taskId) {
        try {
            List<AttachmentDto> attachments = attachmentService.getFilesByTaskId(taskId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Attachments retrieved", 200, attachments));
        } catch (Exception e) {
            log.error("Attachments not retrieved. Error: {}", e.getMessage());
            return ResponseEntity.status(500).body(new BaseResponse<>(false, e.getMessage(), 500, null));
        }
    }
} 