package com.editorial.publications.application.service.impl;

import com.editorial.publications.domain.entity.PublicationStatus;
import com.editorial.publications.infrastructure.exception.PublicationValidationException;
import com.editorial.publications.presentation.dto.CreatePublicationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PublicationValidator {

    public void validateCreatePublication(CreatePublicationDTO createPublicationDTO) {
        log.debug("Validating create publication data: {}", createPublicationDTO.getTitle());

        if (createPublicationDTO.getTitle() == null || createPublicationDTO.getTitle().isBlank()) {
            throw new PublicationValidationException("Title is required");
        }

        if (createPublicationDTO.getTitle().length() > 500) {
            throw new PublicationValidationException("Title cannot exceed 500 characters");
        }

        if (createPublicationDTO.getContent() == null || createPublicationDTO.getContent().isBlank()) {
            throw new PublicationValidationException("Content is required");
        }

        if (createPublicationDTO.getAuthorId() == null || createPublicationDTO.getAuthorId() <= 0) {
            throw new PublicationValidationException("Valid Author ID is required");
        }

        log.debug("Publication validation passed for title: {}", createPublicationDTO.getTitle());
    }

    public void validateStatusTransition(PublicationStatus currentStatus, PublicationStatus targetStatus) {
        log.debug("Validating status transition from {} to {}", currentStatus, targetStatus);

        if (!currentStatus.canTransitionTo(targetStatus)) {
            throw new PublicationValidationException(
                    String.format("Cannot transition from %s to %s", currentStatus, targetStatus)
            );
        }

        log.debug("Status transition validation passed");
    }
}
