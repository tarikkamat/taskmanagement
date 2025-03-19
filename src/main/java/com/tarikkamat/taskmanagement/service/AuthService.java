package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.user.AuthenticateDto;
import com.tarikkamat.taskmanagement.dto.user.LoginResponse;
import com.tarikkamat.taskmanagement.dto.user.RegisterDto;

public interface AuthService {
    LoginResponse authenticate(AuthenticateDto authenticateDto);
    void register(RegisterDto registerDto);
}
