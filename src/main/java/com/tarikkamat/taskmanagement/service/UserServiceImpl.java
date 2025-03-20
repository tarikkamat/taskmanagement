package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.Role;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import com.tarikkamat.taskmanagement.requests.RoleAssignmentRequest;
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
    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    private final User currentUser = (User) authentication.getPrincipal();

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
    public void assignUserToDepartment(String username) {
        User user = userMapper.toEntity(getUserByUsername(username));
        user.setDepartment(currentUser.getDepartment());
        userRepository.save(user);
    }

    @Override
    public void assignUserToRole(String username, RoleAssignmentRequest roleAssignmentRequest) {
        User user = userMapper.toEntity(getUserByUsername(username));

        if (currentUser.getDepartment() != user.getDepartment()) {
            log.error("User {} is not in the same department as the current user", username);
            throw new RuntimeException("User is not in the same department");
        }

        if (roleAssignmentRequest.role() == Role.GROUP_MANAGER && currentUser.getRole() != Role.GROUP_MANAGER) {
            log.error("User {} does not have the required role to assign the role {}", currentUser.getUsername(), roleAssignmentRequest.role());
            throw new RuntimeException("User does not have the required role to assign the role");
        }

        user.setRole(roleAssignmentRequest.role());
        userRepository.save(user);
    }
}
