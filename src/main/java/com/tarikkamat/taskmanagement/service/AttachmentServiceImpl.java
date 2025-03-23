package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.AttachmentDto;
import com.tarikkamat.taskmanagement.entity.Attachment;
import com.tarikkamat.taskmanagement.entity.Task;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.mapper.AttachmentMapper;
import com.tarikkamat.taskmanagement.repository.AttachmentRepository;
import com.tarikkamat.taskmanagement.repository.TaskRepository;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public AttachmentDto uploadFile(UUID taskId, MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Görev bulunamadı"));

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes());

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(filePath.toString());
        attachment.setTask(task);
        attachment.setUploadedBy(user);

        Attachment savedAttachment = attachmentRepository.save(attachment);

        return attachmentMapper.toDto(savedAttachment);
    }

    @Override
    public AttachmentDto getFile(UUID id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dosya bulunamadı"));
        return attachmentMapper.toDto(attachment);
    }

    @Override
    public void deleteFile(UUID id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dosya bulunamadı"));
        try {
            Path path = Paths.get(attachment.getFilePath());
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Dosya silinirken hata oluştu: " + e.getMessage());
        }

        attachmentRepository.delete(attachment);
    }

    @Override
    public List<AttachmentDto> getFilesByTaskId(UUID taskId) {
        List<Attachment> attachments = attachmentRepository.findAllByTaskId(taskId);
        return attachments.stream()
                .map(attachmentMapper::toDto)
                .collect(Collectors.toList());
    }
}
