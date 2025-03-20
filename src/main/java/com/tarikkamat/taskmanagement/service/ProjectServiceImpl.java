package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.mapper.ProjectMapper;
import com.tarikkamat.taskmanagement.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public List<ProjectDto> getAllProjects() {
        List<ProjectDto> projectDtos = projectMapper.toDtoList(projectRepository.findAll());

        if (projectDtos.isEmpty()) {
            throw new RuntimeException("No projects found");
        }

        return projectDtos;
    }
}
