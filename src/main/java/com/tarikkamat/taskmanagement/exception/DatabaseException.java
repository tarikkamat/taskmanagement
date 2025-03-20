package com.tarikkamat.taskmanagement.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
public class DatabaseException extends RuntimeException {
    private final Map<String, String> errors;

    public DatabaseException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public static DatabaseException fromDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        
        if (ex.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cause = (ConstraintViolationException) ex.getCause();
            String constraintName = cause.getConstraintName();

            log.debug("Constraint Name: {}", constraintName);
            log.debug("Error Message: {}", cause.getMessage());
            log.debug("SQL State: {}", cause.getSQLState());

            if (constraintName != null) {
                String fieldName = constraintName.split("_")[1]; // users_email_key -> email
                String readableFieldName = fieldName.replace("_", " ").trim();

                errors.put(fieldName, String.format("This %s is already in use", readableFieldName));
            } else {
                errors.put("unknown", "A database constraint was violated");
            }
        }

        if (errors.isEmpty()) {
            errors.put("unknown", "A database error occurred");
        }

        return new DatabaseException("Data validation failed", errors);
    }
} 