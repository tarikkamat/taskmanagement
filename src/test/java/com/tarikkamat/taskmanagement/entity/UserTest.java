package com.tarikkamat.taskmanagement.entity;

import com.tarikkamat.taskmanagement.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(UUID.randomUUID());
        department.setName("Test Department");

        user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole(Role.TEAM_MEMBER);
        user.setDepartment(department);
        user.setProjects(new ArrayList<>());
    }

    @Test
    void getAuthorities_ShouldReturnCorrectAuthorities() {
        Collection<?> authorities = user.getAuthorities();
        
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(Role.TEAM_MEMBER.name())));
    }

    @Test
    void getUsername_ShouldReturnCorrectUsername() {
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void getPassword_ShouldReturnCorrectPassword() {
        assertEquals("password", user.getPassword());
    }

    @Test
    void toString_ShouldContainAllFields() {
        String userString = user.toString();
        
        assertTrue(userString.contains(user.getFullName()));
        assertTrue(userString.contains(user.getEmail()));
        assertTrue(userString.contains(user.getUsername()));
        assertTrue(userString.contains(user.getRole().toString()));
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        Department newDepartment = new Department();
        newDepartment.setId(UUID.randomUUID());
        newDepartment.setName("New Department");

        user.setFullName("New Name");
        user.setEmail("new@example.com");
        user.setUsername("newuser");
        user.setPassword("newpassword");
        user.setRole(Role.PROJECT_MANAGER);
        user.setDepartment(newDepartment);

        assertEquals("New Name", user.getFullName());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("newuser", user.getUsername());
        assertEquals("newpassword", user.getPassword());
        assertEquals(Role.PROJECT_MANAGER, user.getRole());
        assertEquals(newDepartment, user.getDepartment());
    }

    @Test
    void managedDepartment_ShouldBeAccessible() {
        Department managedDept = new Department();
        managedDept.setId(UUID.randomUUID());
        managedDept.setName("Managed Department");
        managedDept.setManager(user);

        user.setManagedDepartment(managedDept);

        assertEquals(managedDept, user.getManagedDepartment());
    }

    @Test
    void projects_ShouldBeModifiable() {
        Project project1 = new Project();
        project1.setId(UUID.randomUUID());
        Project project2 = new Project();
        project2.setId(UUID.randomUUID());

        List<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);

        user.setProjects(projects);

        assertEquals(2, user.getProjects().size());
        assertTrue(user.getProjects().contains(project1));
        assertTrue(user.getProjects().contains(project2));
    }

    @Test
    void userDetails_AccountNonExpiredShouldBeTrue() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void userDetails_AccountNonLockedShouldBeTrue() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void userDetails_CredentialsNonExpiredShouldBeTrue() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void userDetails_EnabledShouldBeTrue() {
        assertTrue(user.isEnabled());
    }
} 