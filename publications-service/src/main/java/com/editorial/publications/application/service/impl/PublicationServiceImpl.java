package com.editorial.publications.application.service.impl;

import com.editorial.publications.application.mapper.PublicationMapper;
import com.editorial.publications.application.service.IPublicationService;
import com.editorial.publications.domain.entity.Publication;
import com.editorial.publications.domain.entity.PublicationStatus;
import com.editorial.publications.domain.repository.PublicationRepository;
import com.editorial.publications.infrastructure.client.AuthorServiceClient;
import com.editorial.publications.infrastructure.exception.PublicationNotFoundException;
import com.editorial.publications.infrastructure.exception.PublicationValidationException;
import com.editorial.publications.presentation.dto.CreatePublicationDTO;
import com.editorial.publications.presentation.dto.PublicationResponseDTO;
import com.editorial.publications.presentation.dto.UpdatePublicationStatusDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class PublicationServiceImpl implements IPublicationService {

    private final PublicationRepository publicationRepository;
    private final PublicationMapper publicationMapper;
    private final AuthorServiceClient authorServiceClient;
    private final PublicationValidator publicationValidator;

    @Override
    public PublicationResponseDTO createPublication(CreatePublicationDTO createPublicationDTO) {
        log.info("Creating new publication with title: {}", createPublicationDTO.getTitle());

        // Validar datos
        publicationValidator.validateCreatePublication(createPublicationDTO);

        // Validar que el autor exista (comunicación con Authors Service)
        Long authorId = createPublicationDTO.getAuthorId();
        if (!authorServiceClient.authorExists(authorId)) {
            throw new PublicationValidationException("Author not found with id: " + authorId);
        }

        Publication publication = publicationMapper.toEntity(createPublicationDTO);
        publication.setStatus(PublicationStatus.DRAFT);
        Publication savedPublication = publicationRepository.save(publication);

        log.info("Publication created successfully with id: {}", savedPublication.getId());
        return enrichPublicationResponse(savedPublication);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationResponseDTO getPublicationById(Long id) {
        log.debug("Fetching publication with id: {}", id);

        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new PublicationNotFoundException("Publication not found with id: " + id));

        return enrichPublicationResponse(publication);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicationResponseDTO> getAllPublications(Pageable pageable) {
        log.debug("Fetching all publications with pagination: {}", pageable);
        return publicationRepository.findAllActive(pageable)
                .map(this::enrichPublicationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicationResponseDTO> getPublicationsByAuthor(Long authorId, Pageable pageable) {
        log.debug("Fetching publications for author: {}", authorId);
        return publicationRepository.findByAuthorId(authorId, pageable)
                .map(this::enrichPublicationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicationResponseDTO> getPublicationsByStatus(PublicationStatus status, Pageable pageable) {
        log.debug("Fetching publications with status: {}", status);
        return publicationRepository.findByStatus(status, pageable)
                .map(this::enrichPublicationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicationResponseDTO> searchPublications(String keyword, Pageable pageable) {
        log.debug("Searching publications with keyword: {}", keyword);
        return publicationRepository.searchByKeyword(keyword, pageable)
                .map(this::enrichPublicationResponse);
    }

    @Override
    public PublicationResponseDTO updatePublicationStatus(Long id, UpdatePublicationStatusDTO updateDTO) {
        log.info("Updating publication status for id: {} to: {}", id, updateDTO.getStatus());

        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new PublicationNotFoundException("Publication not found with id: " + id));

        publicationValidator.validateStatusTransition(publication.getStatus(), updateDTO.getStatus());

        try {
            publication.updateStatus(updateDTO.getStatus());
            
            if (updateDTO.getReviewerNotes() != null) {
                publication.setReviewerNotes(updateDTO.getReviewerNotes());
            }
            
            if (updateDTO.getRejectionReason() != null) {
                publication.setRejectionReason(updateDTO.getRejectionReason());
            }
            
            Publication updatedPublication = publicationRepository.save(publication);
            log.info("Publication status updated successfully for id: {}", id);
            return enrichPublicationResponse(updatedPublication);

        } catch (IllegalStateException e) {
            log.error("Invalid status transition: {}", e.getMessage());
            throw new PublicationValidationException(e.getMessage());
        }
    }

    @Override
    public void deletePublication(Long id) {
        log.info("Deleting publication with id: {}", id);

        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new PublicationNotFoundException("Publication not found with id: " + id));

        publication.setIsActive(false);
        publicationRepository.save(publication);

        log.info("Publication soft-deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPublications() {
        return publicationRepository.countActive();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPublicationsByStatus(PublicationStatus status) {
        return publicationRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPublicationsByAuthor(Long authorId) {
        return publicationRepository.countByAuthorId(authorId);
    }

    private PublicationResponseDTO enrichPublicationResponse(Publication publication) {
        PublicationResponseDTO dto = publicationMapper.toResponseDTO(publication);
        dto.setSummary(publication.getSummary());

        // Obtener información del autor desde Authors Service
        try {
            AuthorServiceClient.AuthorInfo authorInfo = authorServiceClient.getAuthorById(publication.getAuthorId());
            if (authorInfo != null) {
                dto.setAuthor(PublicationResponseDTO.AuthorDTO.builder()
                        .id(authorInfo.getId())
                        .name(authorInfo.getName())
                        .email(authorInfo.getEmail())
                        .authorType(authorInfo.getAuthorType())
                        .build());
            }
        } catch (Exception e) {
            log.warn("Could not enrich publication with author data: {}", e.getMessage());
            // Continuamos sin datos del autor en lugar de fallar
        }

        return dto;
    }
}
