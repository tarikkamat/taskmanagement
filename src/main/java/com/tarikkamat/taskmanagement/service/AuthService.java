package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.TokenDto;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.requests.LoginRequest;

public interface AuthService {
    TokenDto authenticate(LoginRequest loginRequest);

    void register(UserDto registerUserDto);
}
