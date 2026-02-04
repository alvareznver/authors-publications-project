package com.editorial.authors.presentation.controller;

import com.editorial.authors.application.service.IAuthorService;
import com.editorial.authors.presentation.dto.AuthorResponseDTO;
import com.editorial.authors.presentation.dto.CreateAuthorDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/authors")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthorController {

    private final IAuthorService authorService;

    @PostMapping
    public ResponseEntity<?> createAuthor(@Valid @RequestBody CreateAuthorDTO createAuthorDTO) {
        log.info("POST /authors - Creating new author: {}", createAuthorDTO.getEmail());
        try {
            AuthorResponseDTO response = authorService.createAuthor(createAuthorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating author: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        log.info("GET /authors/{} - Fetching author", id);
        try {
            AuthorResponseDTO response = authorService.getAuthorById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching author: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        log.info("GET /authors - Fetching all authors, page: {}, size: {}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            Page<AuthorResponseDTO> response = authorService.getAllAuthors(pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching authors: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAuthors(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /authors/search - Searching authors with keyword: {}", keyword);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AuthorResponseDTO> response = authorService.searchAuthors(keyword, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching authors: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody CreateAuthorDTO updateAuthorDTO) {
        log.info("PUT /authors/{} - Updating author", id);
        try {
            AuthorResponseDTO response = authorService.updateAuthor(id, updateAuthorDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating author: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id) {
        log.info("DELETE /authors/{} - Deleting author", id);
        try {
            authorService.deleteAuthor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting author: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<?> checkAuthorExists(@PathVariable Long id) {
        log.debug("GET /authors/{}/exists - Checking if author exists", id);
        try {
            boolean exists = authorService.authorExists(id);
            Map<String, Object> response = new HashMap<>();
            response.put("id", id);
            response.put("exists", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking author existence: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stats/total")
    public ResponseEntity<?> getTotalAuthors() {
        log.debug("GET /authors/stats/total - Getting total authors");
        try {
            long total = authorService.getTotalActiveAuthors();
            Map<String, Object> response = new HashMap<>();
            response.put("total", total);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting total authors: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
