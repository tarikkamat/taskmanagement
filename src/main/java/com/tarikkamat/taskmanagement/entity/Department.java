package com.tarikkamat.taskmanagement.entity;

import com.tarikkamat.taskmanagement.common.BaseEntityAudit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "departments")
public class Department extends BaseEntityAudit {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(mappedBy = "department")
    private List<Project> projects;

    @OneToMany(mappedBy = "department")
    private List<User> members;
} 