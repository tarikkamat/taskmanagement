package com.tarikkamat.taskmanagement.enums;

import lombok.Getter;

@Getter
public enum TaskState {
    BACKLOG("Backlog"),
    IN_ANALYSIS("In Analysis"),
    IN_PROGRESS("In Development/Progress"),
    BLOCKED("Blocked"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed");

    private final String displayName;

    TaskState(String displayName) {
        this.displayName = displayName;
    }

}
