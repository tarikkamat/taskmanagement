package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.entity.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    List<ProjectDto> getAllProjects();

    ProjectDto getProjectById(UUID id);

    Project toEntity(ProjectDto projectDto);
}
