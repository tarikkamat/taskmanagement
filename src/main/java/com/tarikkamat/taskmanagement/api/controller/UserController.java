package com.tarikkamat.taskmanagement.api.controller;

import com.tarikkamat.taskmanagement.api.requests.user.ProjectAssignmentRequest;
import com.tarikkamat.taskmanagement.api.requests.user.RoleAssignmentRequest;
import com.tarikkamat.taskmanagement.common.BaseResponse;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.service.UserService;
import jakarta.validation.Valid;
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
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER', 'PROJECT_MANAGER')")
    public ResponseEntity<BaseResponse<List<UserDto>>> getAllUsers() {
        try {
            List<UserDto> users = userService.getAllUsers();
            return ResponseEntity.ok(new BaseResponse<>(true, "Users found", 200, users));
        } catch (Exception e) {
            log.error("Error getting users. Error: {}", e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @GetMapping("/users/{username}")
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER', 'PROJECT_MANAGER', 'TEAM_LEADER') or #username == authentication.name")
    public ResponseEntity<BaseResponse<UserDto>> getUserByUsername(@PathVariable String username) {
        try {
            UserDto user = userService.getUserByUsername(username);
            return ResponseEntity.ok(new BaseResponse<>(true, "User found", 200, user));
        } catch (Exception e) {
            log.error("Error getting user: {}. Error: {}", username, e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @PatchMapping("/users/{username}/department")
    @PreAuthorize("hasAuthority('GROUP_MANAGER')")
    public ResponseEntity<BaseResponse<Void>> assignUserToDepartment(@PathVariable String username) {
        try {
            userService.assignUserToDepartment(username);
            return ResponseEntity.ok(new BaseResponse<>(true, "Department updated", 200, null));
        } catch (Exception e) {
            log.error("Error updating department for user: {}. Error: {}", username, e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @PatchMapping("/users/{username}/role")
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER', 'PROJECT_MANAGER')")
    public ResponseEntity<BaseResponse<Void>> assignUserToRole(@PathVariable String username, @Valid @RequestBody RoleAssignmentRequest roleAssignmentRequest) {
        try {
            userService.assignUserToRole(username, roleAssignmentRequest);
            return ResponseEntity.ok(new BaseResponse<>(true, "Role updated", 200, null));
        } catch (Exception e) {
            log.error("Error updating role for user: {}. Error: {}", username, e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

    @PatchMapping("/users/{username}/project")
    @PreAuthorize("hasAnyAuthority('GROUP_MANAGER','PROJECT_MANAGER')")
    public ResponseEntity<BaseResponse<Void>> assignUserToProject(@PathVariable String username, @Valid @RequestBody ProjectAssignmentRequest projectAssignmentRequest) {
        try {
            userService.assignUserToProject(username, projectAssignmentRequest);
            return ResponseEntity.ok(new BaseResponse<>(true, "Project updated", 200, null));
        } catch (Exception e) {
            log.error("Error updating project for user: {}. Error: {}", username, e.getMessage());
            return ResponseEntity.status(404).body(new BaseResponse<>(false, e.getMessage(), 404, null));
        }
    }

}
