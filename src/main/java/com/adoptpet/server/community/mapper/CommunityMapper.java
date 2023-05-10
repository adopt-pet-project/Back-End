package com.adoptpet.server.community.mapper;

import com.adoptpet.server.community.domain.Community;
import com.adoptpet.server.community.dto.CommunityDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommunityMapper extends GenericMapper<CommunityDto, Community> {
    CommunityMapper INSTANCE = Mappers.getMapper(CommunityMapper.class);


    @Override
    Community toEntity(CommunityDto communityDto);
}

