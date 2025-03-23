package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.api.requests.comment.CommentRequest;
import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.CommentDto;
import com.tarikkamat.taskmanagement.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<BaseResponse<CommentDto>> createComment(@Valid @RequestBody CommentRequest request) {
        try {
            CommentDto comment = commentService.createComment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(true, "Comment created", HttpStatus.CREATED.value(), comment));
        } catch (Exception e) {
            log.error("Comment not created. Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(false, e.getMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
    }
    
    @GetMapping("/task/{taskId}")
    public ResponseEntity<BaseResponse<List<CommentDto>>> getCommentsByTaskId(@PathVariable UUID taskId) {
        try {
            List<CommentDto> comments = commentService.getCommentsByTaskId(taskId);
            return ResponseEntity.ok(new BaseResponse<>(true, "Task comments retrieved", HttpStatus.OK.value(), comments));
        } catch (Exception e) {
            log.error("Task comments not retrieved. Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse<>(false, e.getMessage(), HttpStatus.NOT_FOUND.value(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(@PathVariable UUID id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Comment deleted", HttpStatus.OK.value(), null));
        } catch (Exception e) {
            log.error("Comment not deleted. Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(false, e.getMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CommentDto>> getCommentById(@PathVariable UUID id) {
        try {
            CommentDto comment = commentService.getCommentById(id);
            return ResponseEntity.ok(new BaseResponse<>(true, "Comment found", HttpStatus.OK.value(), comment));
        } catch (Exception e) {
            log.error("Comment not found. Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse<>(false, e.getMessage(), HttpStatus.NOT_FOUND.value(), null));
        }
    }
} 