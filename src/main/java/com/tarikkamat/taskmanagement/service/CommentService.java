package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.comment.CommentRequest;
import com.tarikkamat.taskmanagement.dto.CommentDto;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    CommentDto createComment(CommentRequest request);
    List<CommentDto> getCommentsByTaskId(UUID taskId);
    void deleteComment(UUID id);
    CommentDto getCommentById(UUID id);
}
