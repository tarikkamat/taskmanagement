package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.user.LoginRequest;
import com.tarikkamat.taskmanagement.api.requests.user.RegisterRequest;
import com.tarikkamat.taskmanagement.dto.TokenDto;

public interface AuthService {
    TokenDto authenticate(LoginRequest request);

    void register(RegisterRequest request);
}
