package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.api.requests.comment.CommentRequest;
import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.CommentDto;
import com.tarikkamat.taskmanagement.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
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
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;
    private UUID commentId;
    private UUID taskId;
    private UUID authorId;
    private Date now;
    private CommentDto commentDto;
    private CommentRequest commentRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        commentId = UUID.randomUUID();
        taskId = UUID.randomUUID();
        authorId = UUID.randomUUID();
        now = new Date();

        // Test verilerini oluştur
        commentDto = new CommentDto(
                commentId,
                "testUser",
                "testUser",
                null,
                now,
                now,
                null,
                "Test Comment",
                taskId,
                "Test Task",
                authorId,
                "Test Author"
        );

        commentRequest = new CommentRequest(
                taskId,
                "Test Comment"
        );
    }

    @Test
    void createComment_Success() throws Exception {
        // Arrange
        when(commentService.createComment(any(CommentRequest.class))).thenReturn(commentDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskId\":\"" + taskId + "\",\"content\":\"Test Comment\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Comment created"))
                .andExpect(jsonPath("$.httpStatusCode").value(201))
                .andExpect(jsonPath("$.data.content").value("Test Comment"));
    }

    @Test
    void getCommentsByTaskId_Success() throws Exception {
        // Arrange
        CommentDto comment2 = new CommentDto(
                UUID.randomUUID(),
                "testUser",
                "testUser",
                null,
                now,
                now,
                null,
                "Test Comment 2",
                taskId,
                "Test Task",
                authorId,
                "Test Author"
        );

        when(commentService.getCommentsByTaskId(taskId))
                .thenReturn(Arrays.asList(commentDto, comment2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/comments/task/{taskId}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Task comments retrieved"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].content").value("Test Comment"))
                .andExpect(jsonPath("$.data[1].content").value("Test Comment 2"));
    }

    @Test
    void deleteComment_Success() throws Exception {
        // Arrange
        doNothing().when(commentService).deleteComment(commentId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/comments/{id}", commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Comment deleted"))
                .andExpect(jsonPath("$.httpStatusCode").value(200));

        verify(commentService).deleteComment(commentId);
    }

    @Test
    void getCommentById_Success() throws Exception {
        // Arrange
        when(commentService.getCommentById(commentId)).thenReturn(commentDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/comments/{id}", commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Comment found"))
                .andExpect(jsonPath("$.httpStatusCode").value(200))
                .andExpect(jsonPath("$.data.content").value("Test Comment"));
    }

    @Test
    void createComment_InvalidRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskId\":\"" + taskId + "\",\"content\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCommentsByTaskId_NotFound() throws Exception {
        // Arrange
        when(commentService.getCommentsByTaskId(taskId))
                .thenThrow(new RuntimeException("Task not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/comments/task/{taskId}", taskId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }

    @Test
    void deleteComment_NotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Comment not found"))
                .when(commentService).deleteComment(commentId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/comments/{id}", commentId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Comment not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(400));
    }

    @Test
    void getCommentById_NotFound() throws Exception {
        // Arrange
        when(commentService.getCommentById(commentId))
                .thenThrow(new RuntimeException("Comment not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/comments/{id}", commentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("Comment not found"))
                .andExpect(jsonPath("$.httpStatusCode").value(404));
    }
} 