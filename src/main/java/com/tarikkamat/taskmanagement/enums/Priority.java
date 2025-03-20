package com.tarikkamat.taskmanagement.enums;

import lombok.Getter;

@Getter
public enum Priority {
    CRITICAL("Critical"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

}