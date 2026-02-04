package com.editorial.authors.application.service.impl;

import com.editorial.authors.application.mapper.AuthorMapper;
import com.editorial.authors.application.service.IAuthorService;
import com.editorial.authors.domain.entity.Author;
import com.editorial.authors.domain.repository.AuthorRepository;
import com.editorial.authors.infrastructure.exception.AuthorNotFoundException;
import com.editorial.authors.infrastructure.exception.EmailAlreadyExistsException;
import com.editorial.authors.presentation.dto.AuthorResponseDTO;
import com.editorial.authors.presentation.dto.CreateAuthorDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class AuthorServiceImpl implements IAuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final AuthorValidator authorValidator;

    @Override
    public AuthorResponseDTO createAuthor(CreateAuthorDTO createAuthorDTO) {
        log.info("Creating new author with email: {}", createAuthorDTO.getEmail());

        authorValidator.validateCreateAuthor(createAuthorDTO);

        Author author = authorMapper.toEntity(createAuthorDTO);
        Author savedAuthor = authorRepository.save(author);

        log.info("Author created successfully with id: {}", savedAuthor.getId());
        return mapToResponseDTO(savedAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponseDTO getAuthorById(Long id) {
        log.debug("Fetching author with id: {}", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));

        return mapToResponseDTO(author);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorResponseDTO> getAllAuthors(Pageable pageable) {
        log.debug("Fetching all authors with pagination: {}", pageable);
        return authorRepository.findAllActive(pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorResponseDTO> searchAuthors(String keyword, Pageable pageable) {
        log.debug("Searching authors with keyword: {} and pagination: {}", keyword, pageable);
        return authorRepository.searchByName(keyword, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public AuthorResponseDTO updateAuthor(Long id, CreateAuthorDTO updateAuthorDTO) {
        log.info("Updating author with id: {}", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));

        authorValidator.validateUpdateAuthor(updateAuthorDTO, id);

        authorMapper.updateEntityFromDTO(updateAuthorDTO, author);
        Author updatedAuthor = authorRepository.save(author);

        log.info("Author updated successfully with id: {}", id);
        return mapToResponseDTO(updatedAuthor);
    }

    @Override
    public void deleteAuthor(Long id) {
        log.info("Deleting author with id: {}", id);

        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));

        author.setIsActive(false);
        authorRepository.save(author);

        log.info("Author soft-deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean authorExists(Long id) {
        boolean exists = authorRepository.existsById(id);
        log.debug("Checking if author exists with id: {} - Result: {}", id, exists);
        return exists;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponseDTO> getAuthorsByIds(List<Long> ids) {
        log.debug("Fetching authors by ids: {}", ids);
        return authorRepository.findAllByIds(ids)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalActiveAuthors() {
        return authorRepository.countActive();
    }

    private AuthorResponseDTO mapToResponseDTO(Author author) {
        AuthorResponseDTO dto = authorMapper.toResponseDTO(author);
        dto.setDisplayName(author.getDisplayName());
        return dto;
    }
}
