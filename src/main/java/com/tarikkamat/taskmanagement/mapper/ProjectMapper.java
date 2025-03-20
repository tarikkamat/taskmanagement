package com.tarikkamat.taskmanagement.mapper;

import com.tarikkamat.taskmanagement.dto.ProjectDto;
import com.tarikkamat.taskmanagement.entity.Project;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {
    Project toEntity(ProjectDto projectDto);

    ProjectDto toDto(Project project);

    List<ProjectDto> toDtoList(List<Project> projects);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Project partialUpdate(ProjectDto projectDto, @MappingTarget Project project);
}