package com.adoptpet.server.community.service.mapper;

import com.adoptpet.server.community.domain.Comment;
import com.adoptpet.server.community.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface CreateCommentMapper extends GenericMapper<CommentDto, Comment>{
    CreateCommentMapper INSTANCE =
            Mappers.getMapper(CreateCommentMapper.class);// Mapper class의 instance를 얻어 초기화

    @Override
    CommentDto toDTO(Comment comment);

    @Override
    Comment toEntity(CommentDto commentDto);
}
