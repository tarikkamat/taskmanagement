package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.api.requests.project.UpdateProjectStatusRequest;
import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.entity.Project;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.mapper.ProjectMapper;
import com.tarikkamat.taskmanagement.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        List<ProjectDto> projectDtos = projectMapper.toDtoList(projectRepository.findByDepartment_Id(getCurrentUser().getDepartment().getId()));

        if (projectDtos.isEmpty()) {
            throw new RuntimeException("No projects found");
        }

        return projectDtos;
    }

    @Override
    public ProjectDto getProjectById(UUID id) {
        Project project = projectRepository.findByIdAndDepartment_Id(id, getCurrentUser().getDepartment().getId());

        if (project == null) {
            throw new RuntimeException("Error, not found projectId: " + id);
        }

        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto updateProjectStatus(UUID id, UpdateProjectStatusRequest request) {
        Project project = projectRepository.findByIdAndDepartment_Id(id, getCurrentUser().getDepartment().getId());

        if (project == null) {
            throw new RuntimeException("Error, not found projectId: " + id);
        }

        project.setStatus(request.status());
        projectRepository.save(project);

        return projectMapper.toDto(project);
    }

    @Override
    public Project toEntity(ProjectDto projectDto) {
        return projectMapper.toEntity(projectDto);
    }
}
