package com.tarikkamat.taskmanagement.service;

import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.entity.User;
import com.tarikkamat.taskmanagement.mapper.ProjectMapper;
import com.tarikkamat.taskmanagement.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    private final User currentUser = (User) authentication.getPrincipal();

    @Override
    public List<ProjectDto> getAllProjects() {
        List<ProjectDto> projectDtos = projectMapper.toDtoList(projectRepository.findByDepartment_Id(currentUser.getDepartment().getId()));

        if (projectDtos.isEmpty()) {
            throw new RuntimeException("No projects found");
        }

        return projectDtos;
    }
}
