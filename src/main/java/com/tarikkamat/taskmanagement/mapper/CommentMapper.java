package com.tarikkamat.taskmanagement.mapper;

import com.tarikkamat.taskmanagement.dto.CommentDto;
import com.tarikkamat.taskmanagement.entity.Comment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "taskTitle", source = "task.title")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", expression = "java(comment.getAuthor() != null ? comment.getAuthor().getUsername() : null)")
    CommentDto toDto(Comment comment);

    @Mapping(target = "task", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment toEntity(CommentDto commentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment partialUpdate(CommentDto commentDto, @MappingTarget Comment comment);
}