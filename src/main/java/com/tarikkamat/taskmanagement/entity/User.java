package com.tarikkamat.taskmanagement.entity;

import com.tarikkamat.taskmanagement.common.BaseEntityAudit;
import com.tarikkamat.taskmanagement.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntityAudit implements UserDetails {

    @Column(name = "full_name", nullable = true)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.TEAM_MEMBER;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(mappedBy = "manager")
    private Department managedDepartment;

    @ManyToMany(mappedBy = "teamMembers")
    private List<Project> projects;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role + '\'' +
                ", department=" + department + '\'' +
                ", managedDepartment=" + managedDepartment + '\'' +
                ", projects=" + projects +
                '}' +
                super.toString();
    }
}