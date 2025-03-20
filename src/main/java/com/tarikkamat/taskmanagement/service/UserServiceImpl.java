package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.Project;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.Role;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import com.tarikkamat.taskmanagement.requests.ProjectAssignmentRequest;
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
    private final ProjectService projectService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

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
        user.setDepartment(getCurrentUser().getDepartment());
        userRepository.save(user);
    }

    @Override
    public void assignUserToRole(String username, RoleAssignmentRequest roleAssignmentRequest) {
        User user = userMapper.toEntity(getUserByUsername(username));
        checkUserDepartment(user);

        if (roleAssignmentRequest.role() == Role.GROUP_MANAGER && getCurrentUser().getRole() != Role.GROUP_MANAGER) {
            log.error("User {} does not have the required role to assign the role {}", getCurrentUser().getUsername(), roleAssignmentRequest.role());
            throw new RuntimeException("User does not have the required role to assign the role");
        }

        user.setRole(roleAssignmentRequest.role());
        userRepository.save(user);
    }

    @Override
    public void assignUserToProject(String username, ProjectAssignmentRequest projectAssignmentRequest) {
        User user = userMapper.toEntity(getUserByUsername(username));
        checkUserDepartment(user);

        Project project = projectService.toEntity(projectService.getProjectById(projectAssignmentRequest.projectId()));

        user.getProjects().add(project);
        userRepository.save(user);
    }

    @Override
    public User findByEmailOrUsername(String identifier) {
        return userRepository.findByEmailOrUsername(identifier)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDto toDto(User user) {
        return userMapper.toDto(user);
    }

    private void checkUserDepartment(User user) {
        if (getCurrentUser().getDepartment() != user.getDepartment()) {
            log.error("User {} is not in the same department as the current user", user.getUsername());
            throw new RuntimeException("User is not in the same department");
        }
    }
}
