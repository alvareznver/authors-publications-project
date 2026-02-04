package com.editorial.publications.application.service;

import com.editorial.publications.domain.entity.PublicationStatus;
import com.editorial.publications.presentation.dto.CreatePublicationDTO;
import com.editorial.publications.presentation.dto.PublicationResponseDTO;
import com.editorial.publications.presentation.dto.UpdatePublicationStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPublicationService {

    PublicationResponseDTO createPublication(CreatePublicationDTO createPublicationDTO);

    PublicationResponseDTO getPublicationById(Long id);

    Page<PublicationResponseDTO> getAllPublications(Pageable pageable);

    Page<PublicationResponseDTO> getPublicationsByAuthor(Long authorId, Pageable pageable);

    Page<PublicationResponseDTO> getPublicationsByStatus(PublicationStatus status, Pageable pageable);

    Page<PublicationResponseDTO> searchPublications(String keyword, Pageable pageable);

    PublicationResponseDTO updatePublicationStatus(Long id, UpdatePublicationStatusDTO updateDTO);

    void deletePublication(Long id);

    long getTotalPublications();

    long getTotalPublicationsByStatus(PublicationStatus status);

    long getTotalPublicationsByAuthor(Long authorId);
}
