package com.tarikkamat.taskmanagement.controller;

import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.user.AuthenticateDto;
import com.tarikkamat.taskmanagement.dto.user.LoginResponse;
import com.tarikkamat.taskmanagement.dto.user.RegisterDto;
import com.tarikkamat.taskmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/authenticate")
    public BaseResponse<LoginResponse> authenticate(@Valid @RequestBody AuthenticateDto authenticateDto) {
        try {
            LoginResponse loginResponse = authService.authenticate(authenticateDto);
            return new BaseResponse<>(true, "Login successful", 200, loginResponse);
        } catch (Exception e) {
            return new BaseResponse<>(false, e.getMessage(), 401, null);
        }
    }

    @PostMapping("/register")
    public BaseResponse<Void> register(@Valid @RequestBody RegisterDto registerDto) {
        try {
            authService.register(registerDto);
            return new BaseResponse<>(true, "Registration successful", 201, null);
        } catch (Exception e) {
            throw e;
        }
    }
}
