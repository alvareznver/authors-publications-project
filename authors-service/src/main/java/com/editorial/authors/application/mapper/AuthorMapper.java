package com.editorial.authors.application.mapper;

import com.editorial.authors.domain.entity.Author;
import com.editorial.authors.presentation.dto.AuthorResponseDTO;
import com.editorial.authors.presentation.dto.CreateAuthorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(source = "id", target = "id")
    AuthorResponseDTO toResponseDTO(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "publicationsCount", ignore = true)
    Author toEntity(CreateAuthorDTO createAuthorDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "publicationsCount", ignore = true)
    void updateEntityFromDTO(CreateAuthorDTO createAuthorDTO, @MappingTarget Author author);
}
