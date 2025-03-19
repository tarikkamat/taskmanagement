package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto getUserByUsername(String username);
    void createUser(UserDto userDto);
    Optional<List<UserDto>> getAllUsers();
}
