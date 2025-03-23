package com.tarikkamat.taskmanagement.config;

import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.Department;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.enums.Role;
import com.tarikkamat.taskmanagement.mapper.UserMapper;
import com.tarikkamat.taskmanagement.repository.DepartmentRepository;
import com.tarikkamat.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    @Value("${app.demo-content.enabled:false}")
    private boolean demoContentEnabled;

    @Override
    @Transactional
    public void run(String... args) {
        if (demoContentEnabled && userRepository.count() == 0) {
            // Create departments
            Department softwareDepartment = createDepartment("Software Development");
            Department marketingDepartment = createDepartment("Marketing");

            // Create project manager for Software Department
            createAndSaveUser("Software Project Manager", "project.manager@example.com", "projectmanager", "Manager123!", Role.PROJECT_MANAGER, softwareDepartment);
            
            // Create department managers
            User softwareManager = createAndSaveUser("Software Manager", "software.manager@example.com", "softwaremanager", "Manager123!", Role.GROUP_MANAGER, softwareDepartment);
            User marketingManager = createAndSaveUser("Marketing Manager", "marketing.manager@example.com", "marketingmanager", "Manager123!", Role.GROUP_MANAGER, marketingDepartment);

            // Set department managers
            softwareDepartment.setManager(softwareManager);
            marketingDepartment.setManager(marketingManager);

            // Save departments
            softwareDepartment = departmentRepository.save(softwareDepartment);
            marketingDepartment = departmentRepository.save(marketingDepartment);

            // Set managed departments for managers
            softwareManager.setDepartment(softwareDepartment);
            marketingManager.setDepartment(marketingDepartment);
            userRepository.save(softwareManager);
            userRepository.save(marketingManager);

            // Create team leaders for Software Department
            List<User> softwareTeamLeaders = new ArrayList<>();
            softwareTeamLeaders.add(createAndSaveUser("Backend Team Leader", "backend.lead@example.com", "backendlead", "Leader123!", Role.TEAM_LEADER, softwareDepartment));
            softwareTeamLeaders.add(createAndSaveUser("Frontend Team Leader", "frontend.lead@example.com", "frontendlead", "Leader123!", Role.TEAM_LEADER, softwareDepartment));

            // Create team leaders for Marketing Department
            List<User> marketingTeamLeaders = new ArrayList<>();
            marketingTeamLeaders.add(createAndSaveUser("Digital Marketing Lead", "digital.lead@example.com", "digitallead", "Leader123!", Role.TEAM_LEADER, marketingDepartment));
            marketingTeamLeaders.add(createAndSaveUser("Content Marketing Lead", "content.lead@example.com", "contentlead", "Leader123!", Role.TEAM_LEADER, marketingDepartment));

            // Create team members for Software Department
            createAndSaveUser("Backend Developer 1", "backend1@example.com", "backend1", "Member123!", Role.TEAM_MEMBER, softwareDepartment);
            createAndSaveUser("Backend Developer 2", "backend2@example.com", "backend2", "Member123!", Role.TEAM_MEMBER, softwareDepartment);
            createAndSaveUser("Frontend Developer 1", "frontend1@example.com", "frontend1", "Member123!", Role.TEAM_MEMBER, softwareDepartment);
            createAndSaveUser("Frontend Developer 2", "frontend2@example.com", "frontend2", "Member123!", Role.TEAM_MEMBER, softwareDepartment);

            // Create team members for Marketing Department
            createAndSaveUser("Digital Marketing Specialist 1", "digital1@example.com", "digital1", "Member123!", Role.TEAM_MEMBER, marketingDepartment);
            createAndSaveUser("Digital Marketing Specialist 2", "digital2@example.com", "digital2", "Member123!", Role.TEAM_MEMBER, marketingDepartment);
            createAndSaveUser("Content Specialist 1", "content1@example.com", "content1", "Member123!", Role.TEAM_MEMBER, marketingDepartment);
            createAndSaveUser("Content Specialist 2", "content2@example.com", "content2", "Member123!", Role.TEAM_MEMBER, marketingDepartment);
        }
    }

    private Department createDepartment(String name) {
        Department department = new Department();
        department.setName(name);
        department.setDescription("Default department: " + name);
        return department;
    }


    private User createAndSaveUser(String fullName, String email, String username, String password, Role role, Department department) {
        UserDto userDto = new UserDto(null, fullName, email, username, password, role);
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(password));
        user.setDepartment(department);
        return userRepository.save(user);
    }
} 