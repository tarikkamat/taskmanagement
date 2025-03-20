package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserByUsername(String username) {
        return userMapper.toDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public void createUser(UserDto userDto) {
        userRepository.save(userMapper.toEntity(userDto));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtos = userMapper.toDtoList(userRepository.findAll());

        if (userDtos.isEmpty()) {
            throw new RuntimeException("No users found");
        }

        return userDtos;
    }

    @Override
    public void updateDepartment(String username) {
        log.info("Starting department update process for user: {}", username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        log.info("Request made by user: {} with role: {}", currentUser.getUsername(), currentUser.getRole());
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new RuntimeException("User not found");
                });
                
        log.info("Setting department {} for user {}", currentUser.getDepartment().getName(), username);
        user.setDepartment(currentUser.getDepartment());
        userRepository.save(user);
        log.info("Department update completed successfully for user: {}", username);
    }
}
