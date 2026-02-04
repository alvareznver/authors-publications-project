package com.editorial.authors.application.service.impl;

import com.editorial.authors.domain.repository.AuthorRepository;
import com.editorial.authors.infrastructure.exception.EmailAlreadyExistsException;
import com.editorial.authors.infrastructure.exception.InvalidAuthorDataException;
import com.editorial.authors.presentation.dto.CreateAuthorDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class AuthorValidator {

    private final AuthorRepository authorRepository;

    public void validateCreateAuthor(CreateAuthorDTO createAuthorDTO) {
        log.debug("Validating create author data: {}", createAuthorDTO.getEmail());

        if (createAuthorDTO.getName() == null || createAuthorDTO.getName().isBlank()) {
            throw new InvalidAuthorDataException("Author name is required");
        }

        if (createAuthorDTO.getEmail() == null || createAuthorDTO.getEmail().isBlank()) {
            throw new InvalidAuthorDataException("Email is required");
        }

        if (createAuthorDTO.getAuthorType() == null) {
            throw new InvalidAuthorDataException("Author type is required");
        }

        if (authorRepository.findByEmail(createAuthorDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists: " + createAuthorDTO.getEmail());
        }

        if (!isValidEmail(createAuthorDTO.getEmail())) {
            throw new InvalidAuthorDataException("Invalid email format");
        }

        log.debug("Author validation passed for email: {}", createAuthorDTO.getEmail());
    }

    public void validateUpdateAuthor(CreateAuthorDTO updateAuthorDTO, Long authorId) {
        log.debug("Validating update author data for id: {}", authorId);

        if (updateAuthorDTO.getEmail() != null && !updateAuthorDTO.getEmail().isBlank()) {
            if (!isValidEmail(updateAuthorDTO.getEmail())) {
                throw new InvalidAuthorDataException("Invalid email format");
            }

            if (authorRepository.existsByEmailAndIdNot(updateAuthorDTO.getEmail(), authorId)) {
                throw new EmailAlreadyExistsException("Email already exists: " + updateAuthorDTO.getEmail());
            }
        }

        log.debug("Author update validation passed for id: {}", authorId);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }
}
