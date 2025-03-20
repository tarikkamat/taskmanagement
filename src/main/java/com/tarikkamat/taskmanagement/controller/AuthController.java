package com.tarikkamat.taskmanagement.controller;

import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.TokenDto;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.requests.LoginRequest;
import com.tarikkamat.taskmanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<BaseResponse<TokenDto>> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            TokenDto tokenDto = authService.authenticate(loginRequest);
            return ResponseEntity.ok(new BaseResponse<>(true, "Login successful", 200, tokenDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BaseResponse<>(false, e.getMessage(), 401, null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Void>> register(@Valid @RequestBody UserDto registerUserDto) {
        try {
            authService.register(registerUserDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BaseResponse<>(true, "Registration successful", 201, null));
        } catch (Exception e) {
            throw e;
        }
    }
}
