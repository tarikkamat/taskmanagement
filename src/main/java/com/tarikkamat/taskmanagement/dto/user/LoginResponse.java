package com.tarikkamat.taskmanagement.dto.user;

import java.io.Serializable;

public record LoginResponse(
        String accessToken,
        long expirationAt
) implements Serializable {
}