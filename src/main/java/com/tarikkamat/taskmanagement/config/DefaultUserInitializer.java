package com.tarikkamat.taskmanagement.config;

import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.Role;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            List<UserDto> defaultUsers = Arrays.asList(
                createDefaultUser("Project Manager", "project.manager@example.com", "projectmanager", "ProjectManager123!", Role.PROJECT_MANAGER),
                createDefaultUser("Team Leader", "team.leader@example.com", "teamleader", "TeamLeader123!", Role.TEAM_LEADER),
                createDefaultUser("Team Member", "team.member@example.com", "teammember", "TeamMember123!", Role.TEAM_MEMBER)
            );

            defaultUsers.forEach(userDto -> {
                User user = userMapper.toEntity(userDto);
                user.setPassword(passwordEncoder.encode(userDto.password()));
                userRepository.save(user);
            });
        }
    }

    private UserDto createDefaultUser(String fullName, String email, String username, String password, Role role) {
        return new UserDto(null, fullName, email, username, password, role);
    }
} 