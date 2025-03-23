package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.AttachmentDto;
import com.tarikkamat.taskmanagement.service.AttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AttachmentControllerTest {

    @Mock
    private AttachmentService attachmentService;

    @InjectMocks
    private AttachmentController attachmentController;

    private MockMvc mockMvc;
    private UUID taskId;
    private UUID attachmentId;
    private Date now;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();
        taskId = UUID.randomUUID();
        attachmentId = UUID.randomUUID();
        now = new Date();
    }

    @Test
    void uploadFile_Success() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes()
        );

        AttachmentDto attachmentDto = new AttachmentDto(
                attachmentId,
                "testUser",
                "testUser",
                null,
                now,
                now,
                null,
                "test.txt",
                "/path/to/file"
        );

        when(attachmentService.uploadFile(any(UUID.class), any())).thenReturn(attachmentDto);

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/attachments/upload/{taskId}", taskId)
                        .file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Attachment uploaded"))
                .andExpect(jsonPath("$.httpStatusCode").value(201))
                .andExpect(jsonPath("$.data.fileName").value("test.txt"));
    }

    @Test
    void downloadFile_Success() throws Exception {
        // Arrange
        AttachmentDto attachmentDto = new AttachmentDto(
                attachmentId,
                "testUser",
                "testUser",
                null,
                now,
                now,
                null,
                "test.txt",
                "/path/to/file"
        );

        when(attachmentService.getFile(any(UUID.class))).thenReturn(attachmentDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/attachments/{id}", attachmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Attachment retrieved"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.fileName").value("test.txt"));
    }

    @Test
    void deleteFile_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/attachments/{id}", attachmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Attachment deleted"))
                .andExpect(jsonPath("$.httpStatusCode").value(200));

        verify(attachmentService).deleteFile(attachmentId);
    }

    @Test
    void getFilesByTaskId_Success() throws Exception {
        // Arrange
        AttachmentDto attachment1 = new AttachmentDto(
                UUID.randomUUID(),
                "testUser",
                "testUser",
                null,
                now,
                now,
                null,
                "test1.txt",
                "/path/to/file1"
        );

        AttachmentDto attachment2 = new AttachmentDto(
                UUID.randomUUID(),
                "testUser",
                "testUser",
                null,
                now,
                now,
                null,
                "test2.txt",
                "/path/to/file2"
        );

        when(attachmentService.getFilesByTaskId(any(UUID.class)))
                .thenReturn(Arrays.asList(attachment1, attachment2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/attachments/task/{taskId}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Attachments retrieved"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].fileName").value("test1.txt"))
                .andExpect(jsonPath("$.data[1].fileName").value("test2.txt"));
    }

    @Test
    void uploadFile_Error() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes()
        );

        when(attachmentService.uploadFile(any(UUID.class), any()))
                .thenThrow(new RuntimeException("Upload failed"));

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/attachments/upload/{taskId}", taskId)
                        .file(file))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Upload failed"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }
} 