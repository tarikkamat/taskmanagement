package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.requests.RoleAssignmentRequest;

import java.util.List;

public interface UserService {
    UserDto getUserByUsername(String username);

    void createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    void assignUserToDepartment(String username);

    void assignUserToRole(String username, RoleAssignmentRequest role);
}
