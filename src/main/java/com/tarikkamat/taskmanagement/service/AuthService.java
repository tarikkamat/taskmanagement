package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.AuthenticateDto;
import com.tarikkamat.taskmanagement.dto.TokenDto;
import com.tarikkamat.taskmanagement.dto.UserDto;

public interface AuthService {
    TokenDto authenticate(AuthenticateDto authenticateDto);
    void register(UserDto registerUserDto);
}
