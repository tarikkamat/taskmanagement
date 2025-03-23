package com.tarikkamat.taskmanagement.mapper;

import com.tarikkamat.taskmanagement.api.requests.task.CreateTaskRequest;
import com.tarikkamat.taskmanagement.api.requests.task.UpdateTaskRequest;
import com.tarikkamat.taskmanagement.dto.TaskDto;
import com.tarikkamat.taskmanagement.entity.Task;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    Task toEntity(TaskDto taskDto);
    TaskDto toDto(Task task);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(source = "userStoryDescription", target = "userStoryDescription")
    @Mapping(source = "acceptanceCriteria", target = "acceptanceCriteria")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "stateReason", target = "stateReason")
    TaskDto toDto(CreateTaskRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(source = "userStoryDescription", target = "userStoryDescription")
    @Mapping(source = "acceptanceCriteria", target = "acceptanceCriteria")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "stateReason", target = "stateReason")
    TaskDto toDto(UpdateTaskRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task partialUpdate(TaskDto taskDto, @MappingTarget Task task);
}