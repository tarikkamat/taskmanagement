package com.tarikkamat.taskmanagement.controller;

import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.user.UserDto;
import com.tarikkamat.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<BaseResponse<List<UserDto>>> getAllUsers() {
        try{
            List<UserDto> users = userService.getAllUsers().orElseThrow(() -> new Exception("No users found"));
            return ResponseEntity.ok(new BaseResponse<>(true, "Users found", 200, users));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<BaseResponse<UserDto>> getUserByUsername(@PathVariable String username) {
        try {
            UserDto user = userService.getUserByUsername(username);
            return ResponseEntity.ok(new BaseResponse<>(true, "User found", 200, user));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }



}
