package com.tarikkamat.taskmanagement.enums;

import lombok.Getter;

@Getter
public enum Role {
    GROUP_MANAGER("Group Manager"),
    PROJECT_MANAGER("Project Manager"),
    TEAM_LEADER("Team Leader"),
    TEAM_MEMBER("Team Member");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

}
