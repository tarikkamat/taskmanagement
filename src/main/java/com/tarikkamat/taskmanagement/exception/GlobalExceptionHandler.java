package com.tarikkamat.taskmanagement.exception;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.tarikkamat.taskmanagement.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new BaseResponse<>(false, "You don't have permission for this operation", 403, null));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication error: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new BaseResponse<>(false, "You need to be authenticated", 401, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(
                new BaseResponse<>(false, "Validation failed", HttpStatus.BAD_REQUEST.value(), errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex.getCause() instanceof InvalidDefinitionException) {
            InvalidDefinitionException ide = (InvalidDefinitionException) ex.getCause();
            String fieldName = ide.getPath().get(ide.getPath().size() - 1).getFieldName();
            errors.put(fieldName, "This field is required");
        } else {
            errors.put("body", "Request body is missing or invalid");
        }

        return new ResponseEntity<>(
                new BaseResponse<>(false, "Invalid request", HttpStatus.BAD_REQUEST.value(), errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(
                new BaseResponse<>(false, "Validation failed", HttpStatus.BAD_REQUEST.value(), ex.getErrors()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(
                new BaseResponse<>(false, ex.getMessage(), HttpStatus.NOT_FOUND.value(), null),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(
                new BaseResponse<>(false, "Invalid username or password", HttpStatus.UNAUTHORIZED.value(), null),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<Void>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String message = String.format(
                "%s method is not supported for this request. Supported methods are: %s",
                ex.getMethod(),
                String.join(", ", ex.getSupportedMethods())
        );

        return new ResponseEntity<>(
                new BaseResponse<>(false, message, HttpStatus.METHOD_NOT_ALLOWED.value(), null),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();
            String constraintName = cause.getConstraintName();

            if (constraintName != null) {
                if (constraintName.contains("email")) {
                    errors.put("email", "This email is already registered");
                } else if (constraintName.contains("username")) {
                    errors.put("username", "This username is already taken");
                } else {
                    Pattern pattern = Pattern.compile("users_(.+)_key");
                    Matcher matcher = pattern.matcher(constraintName);
                    if (matcher.find()) {
                        String fieldName = matcher.group(1);
                        errors.put(fieldName, "This " + fieldName + " is already in use");
                    } else {
                        errors.put("unknown", "A database constraint was violated");
                    }
                }
            }
        }

        if (errors.isEmpty()) {
            errors.put("unknown", "A database error occurred");
        }

        return new ResponseEntity<>(
                new BaseResponse<>(false, "Data validation failed", HttpStatus.CONFLICT.value(), errors),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse<>(false, "An unexpected error occurred", 500, null));
    }
} 