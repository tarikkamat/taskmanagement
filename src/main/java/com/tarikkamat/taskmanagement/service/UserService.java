package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserByUsername(String username);

    void createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    void updateDepartment(String username);
}
