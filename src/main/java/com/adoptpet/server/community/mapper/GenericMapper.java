package com.adoptpet.server.community.mapper;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
public interface GenericMapper<DTO,Entity>{

    DTO toDTO(Entity entity);

    Entity toEntity(DTO dto);

    ArrayList<DTO> toDtoList(List<Entity> list);

    ArrayList<Entity> toEntityList(List<DTO> dtoList);

    /* Null 값이 전달될 경우 변화 시키지 않도록 설정 */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(DTO dto, @MappingTarget Entity entity);
}
