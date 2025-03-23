package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.AttachmentDto;
import com.tarikkamat.taskmanagement.entity.Attachment;
import com.tarikkamat.taskmanagement.entity.Task;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.mapper.AttachmentMapper;
import com.tarikkamat.taskmanagement.repository.AttachmentRepository;
import com.tarikkamat.taskmanagement.repository.TaskRepository;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceImplTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private AttachmentMapper attachmentMapper;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    private User mockUser;
    private Task mockTask;
    private Attachment mockAttachment;
    private AttachmentDto mockAttachmentDto;
    private UUID taskId;
    private UUID attachmentId;
    private String username;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        attachmentId = UUID.randomUUID();
        username = "testUser";

        mockUser = new User();
        mockUser.setUsername(username);

        mockTask = new Task();
        mockTask.setId(taskId);

        mockAttachment = new Attachment();
        mockAttachment.setId(attachmentId);
        mockAttachment.setFileName("test.txt");
        mockAttachment.setFilePath("uploads/test.txt");
        mockAttachment.setTask(mockTask);
        mockAttachment.setUploadedBy(mockUser);

        mockAttachmentDto = new AttachmentDto(
            attachmentId,
            username,
            null,
            null,
            new Date(),
            null,
            null,
            "test.txt",
            "uploads/test.txt"
        );
        
        ReflectionTestUtils.setField(attachmentService, "uploadDir", "test-uploads");
    }

    @Test
    void uploadFile_Success() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.txt", "text/plain", "test content".getBytes()
        );

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(mockAttachment);
        when(attachmentMapper.toDto(mockAttachment)).thenReturn(mockAttachmentDto);

        // Act
        AttachmentDto result = attachmentService.uploadFile(taskId, file);

        // Assert
        assertNotNull(result);
        assertEquals(mockAttachmentDto.fileName(), result.fileName());
        assertEquals(mockAttachmentDto.filePath(), result.filePath());
        verify(attachmentRepository).save(any(Attachment.class));
    }

    @Test
    void getFile_Success() {
        // Arrange
        when(attachmentRepository.findById(attachmentId)).thenReturn(Optional.of(mockAttachment));
        when(attachmentMapper.toDto(mockAttachment)).thenReturn(mockAttachmentDto);

        // Act
        AttachmentDto result = attachmentService.getFile(attachmentId);

        // Assert
        assertNotNull(result);
        assertEquals(mockAttachmentDto.fileName(), result.fileName());
        assertEquals(mockAttachmentDto.filePath(), result.filePath());
    }

    @Test
    void deleteFile_Success() throws IOException {
        // Arrange
        when(attachmentRepository.findById(attachmentId)).thenReturn(Optional.of(mockAttachment));
        doNothing().when(attachmentRepository).delete(mockAttachment);

        // Act
        attachmentService.deleteFile(attachmentId);

        // Assert
        verify(attachmentRepository).delete(mockAttachment);
    }

    @Test
    void getFilesByTaskId_Success() {
        // Arrange
        when(attachmentRepository.findAllByTaskId(taskId))
            .thenReturn(List.of(mockAttachment));
        when(attachmentMapper.toDto(mockAttachment)).thenReturn(mockAttachmentDto);

        // Act
        List<AttachmentDto> result = attachmentService.getFilesByTaskId(taskId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mockAttachmentDto.fileName(), result.get(0).fileName());
    }
} 