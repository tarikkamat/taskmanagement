package com.tarikkamat.taskmanagement.controller;

import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<BaseResponse<List<UserDto>>> getAllUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            return ResponseEntity.ok(new BaseResponse<>(true, "Users found", 200, users));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<BaseResponse<UserDto>> getUserByUsername(@PathVariable String username) {
        try {
            UserDto user = userService.getUserByUsername(username);
            return ResponseEntity.ok(new BaseResponse<>(true, "User found", 200, user));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @PatchMapping("/users/{username}/department")
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER', 'PROJECT_MANAGER', 'TEAM_LEADER')")
    public ResponseEntity<BaseResponse<Void>> updateDepartment(@PathVariable String username) {
        log.info("Department update request received for user: {}", username);
        try {
            userService.updateDepartment(username);
            log.info("Department successfully updated for user: {}", username);
            return ResponseEntity.ok(new BaseResponse<>(true, "Department updated", 200, null));
        } catch (Exception e) {
            log.error("Error updating department for user: {}. Error: {}", username, e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

}
