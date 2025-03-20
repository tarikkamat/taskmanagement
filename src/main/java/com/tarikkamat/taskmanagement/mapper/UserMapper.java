package com.tarikkamat.taskmanagement.mapper;

import com.tarikkamat.taskmanagement.dto.UserDto;
import com.tarikkamat.taskmanagement.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
    List<UserDto> toDtoList(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}