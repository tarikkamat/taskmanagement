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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentRequest request) {
        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new EntityNotFoundException("Error, not found taskId: " + request.taskId()));

        User currentUser = getCurrentUser();

        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setTask(task);
        comment.setAuthor(currentUser);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByTaskId(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Error, not found taskId: " + taskId));

        return task.getComments().stream()
                .filter(comment -> comment.getDeletedAt() == null)
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error, not found commentId: " + id));

        User currentUser = getCurrentUser();

        if (!comment.getAuthor().getId().equals(currentUser.getId()) &&
                !comment.getTask().getAssignee().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Error, you are not authorized to delete this comment");
        }

        comment.setDeletedAt(new Date());
        comment.setDeletedBy(currentUser.getUsername());
        commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error, not found commentId: " + id));

        if (comment.getDeletedAt() != null) {
            throw new EntityNotFoundException("Error, not found commentId: " + id);
        }

        return commentMapper.toDto(comment);
    }
}
