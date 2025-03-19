package com.tarikkamat.taskmanagement.dto;

import java.io.Serializable;

public record TokenDto(
        String accessToken,
        long expirationIn
) implements Serializable {
}