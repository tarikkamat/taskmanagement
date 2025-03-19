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
    private String title;
    private String description;
    private String departmentName;

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
