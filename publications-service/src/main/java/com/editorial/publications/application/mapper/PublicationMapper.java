package com.editorial.publications.application.mapper;

import com.editorial.publications.domain.entity.Publication;
import com.editorial.publications.presentation.dto.CreatePublicationDTO;
import com.editorial.publications.presentation.dto.PublicationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PublicationMapper {

    @Mapping(source = "id", target = "id")
    PublicationResponseDTO toResponseDTO(Publication publication);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "viewsCount", ignore = true)
    @Mapping(target = "reviewerNotes", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    Publication toEntity(CreatePublicationDTO createPublicationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDTO(CreatePublicationDTO createPublicationDTO, @MappingTarget Publication publication);
}
