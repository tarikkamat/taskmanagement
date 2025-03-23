package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.comment.CommentRequest;
import com.tarikkamat.taskmanagement.dto.CommentDto;
import com.tarikkamat.taskmanagement.entity.Comment;
import com.tarikkamat.taskmanagement.entity.Task;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.mapper.CommentMapper;
import com.tarikkamat.taskmanagement.repository.CommentRepository;
import com.tarikkamat.taskmanagement.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User mockUser;
    private Task mockTask;
    private Comment mockComment;
    private CommentDto mockCommentDto;
    private UUID taskId;
    private UUID commentId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        commentId = UUID.randomUUID();

        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setUsername("testUser");

        mockTask = new Task();
        mockTask.setId(taskId);

        mockComment = new Comment();
        mockComment.setId(commentId);
        mockComment.setContent("Test comment");
        mockComment.setTask(mockTask);
        mockComment.setAuthor(mockUser);
        mockComment.setCreatedAt(new Date());

        mockCommentDto = new CommentDto(
            commentId,
            mockUser.getUsername(),
            null,
            null,
            new Date(),
            null,
            null,
            "Test comment",
            taskId,
            "Test Task",
            mockUser.getId(),
            mockUser.getUsername()
        );
    }

    @Test
    void createComment_Success() {
        // Arrange
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        CommentRequest request = new CommentRequest(taskId, "Test comment");
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);
        when(commentMapper.toDto(mockComment)).thenReturn(mockCommentDto);

        // Act
        CommentDto result = commentService.createComment(request);

        // Assert
        assertNotNull(result);
        assertEquals(mockCommentDto.content(), result.content());
        assertEquals(mockCommentDto.taskId(), result.taskId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void getCommentsByTaskId_Success() {
        // Arrange
        mockTask.setComments(List.of(mockComment));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));
        when(commentMapper.toDto(mockComment)).thenReturn(mockCommentDto);

        // Act
        List<CommentDto> result = commentService.getCommentsByTaskId(taskId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mockCommentDto.content(), result.get(0).content());
    }

    @Test
    void deleteComment_Success() {
        // Arrange
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepository).save(mockComment);
        assertNotNull(mockComment.getDeletedAt());
        assertEquals(mockUser.getUsername(), mockComment.getDeletedBy());
    }

    @Test
    void getCommentById_Success() {
        // Arrange
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        when(commentMapper.toDto(mockComment)).thenReturn(mockCommentDto);

        // Act
        CommentDto result = commentService.getCommentById(commentId);

        // Assert
        assertNotNull(result);
        assertEquals(mockCommentDto.content(), result.content());
        assertEquals(mockCommentDto.taskId(), result.taskId());
    }

    @Test
    void deleteComment_Unauthorized() {
        // Arrange
        User differentUser = new User();
        differentUser.setId(UUID.randomUUID());
        differentUser.setUsername("differentUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(differentUser);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> commentService.deleteComment(commentId));
    }
} 