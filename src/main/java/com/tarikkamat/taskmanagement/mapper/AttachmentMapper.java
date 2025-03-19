package com.tarikkamat.taskmanagement.mapper;

import com.tarikkamat.taskmanagement.dto.AttachmentDto;
import com.tarikkamat.taskmanagement.entity.Attachment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttachmentMapper {
    Attachment toEntity(AttachmentDto attachmentDto);

    AttachmentDto toDto(Attachment attachment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Attachment partialUpdate(AttachmentDto attachmentDto, @MappingTarget Attachment attachment);
}