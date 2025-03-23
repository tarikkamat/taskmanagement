package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.user.ProjectAssignmentRequest;
import com.tarikkamat.taskmanagement.api.requests.user.RoleAssignmentRequest;
import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.User;

import java.util.List;

public interface UserService {
    UserDto getUserByUsername(String username);

    void createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    void assignUserToDepartment(String username);

    void assignUserToRole(String username, RoleAssignmentRequest roleAssignmentRequest);

    void assignUserToProject(String username, ProjectAssignmentRequest projectAssignmentRequest);

    UserDto toDto(User user);

    User findByEmailOrUsername(String identifier);
}
