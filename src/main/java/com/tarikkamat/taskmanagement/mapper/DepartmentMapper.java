package com.tarikkamat.taskmanagement.mapper;

import com.tarikkamat.taskmanagement.dto.DepartmentDto;
import com.tarikkamat.taskmanagement.entity.Department;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {
    Department toEntity(DepartmentDto departmentDto);

    DepartmentDto toDto(Department department);

    List<DepartmentDto> toDtoList(List<Department> departments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Department partialUpdate(DepartmentDto departmentDto, @MappingTarget Department department);
}