package com.tarikkamat.taskmanagement.entity;

import com.tarikkamat.taskmanagement.common.BaseEntityAudit;
import com.tarikkamat.taskmanagement.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class Project extends BaseEntityAudit {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private User projectManager;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToMany
    @JoinTable(
            name = "project_team_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> teamMembers;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;
}
