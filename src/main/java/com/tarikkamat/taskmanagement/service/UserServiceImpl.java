package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.user.UserDto;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserByUsername(String username){
        return userMapper.toDto(userRepository.findByUsername(username).orElseThrow());
    }

    @Override
    public void createUser(UserDto userDto){
        userRepository.save(userMapper.toEntity(userDto));
    }
}
