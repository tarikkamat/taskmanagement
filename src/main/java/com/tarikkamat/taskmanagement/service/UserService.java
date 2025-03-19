package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.user.UserDto;

public interface UserService {
    UserDto getUserByUsername(String username);
    void createUser(UserDto userDto);
}
