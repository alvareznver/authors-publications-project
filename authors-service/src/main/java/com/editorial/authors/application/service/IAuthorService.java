package com.editorial.authors.application.service;

import com.editorial.authors.presentation.dto.AuthorResponseDTO;
import com.editorial.authors.presentation.dto.CreateAuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAuthorService {

    AuthorResponseDTO createAuthor(CreateAuthorDTO createAuthorDTO);

    AuthorResponseDTO getAuthorById(Long id);

    Page<AuthorResponseDTO> getAllAuthors(Pageable pageable);

    Page<AuthorResponseDTO> searchAuthors(String keyword, Pageable pageable);

    AuthorResponseDTO updateAuthor(Long id, CreateAuthorDTO updateAuthorDTO);

    void deleteAuthor(Long id);

    boolean authorExists(Long id);

    List<AuthorResponseDTO> getAuthorsByIds(List<Long> ids);

    long getTotalActiveAuthors();
}
