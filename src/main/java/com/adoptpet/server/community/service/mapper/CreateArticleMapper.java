package com.adoptpet.server.community.service.mapper;

import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.ArticleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

// 해당 매퍼를 스프링 빈(bean)으로 등록하기 위한 설정
@Mapper(componentModel = "spring",
        // 매핑 과정 중에 null 값이 발견되면 매핑 작업이 결과로 null 값을 반환해야 함을 나타냄.
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface CreateArticleMapper extends GenericMapper<ArticleDto, Community> {
    CreateArticleMapper INSTANCE =
            Mappers.getMapper(CreateArticleMapper.class);// Mapper class의 instance를 얻어 초기화

    @Override
    Community toEntity(ArticleDto articleDto); // plan method - @Mapping 으로 옵션 지정가능

    @Override
    @Mapping(target = "image", ignore = true) // imgNo은 매핑 제외
    ArticleDto toDTO(Community community);
}
