package com.tarikkamat.taskmanagement.enums;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

}