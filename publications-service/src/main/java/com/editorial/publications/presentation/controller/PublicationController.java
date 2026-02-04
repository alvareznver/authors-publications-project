package com.editorial.publications.presentation.controller;

import com.editorial.publications.application.service.IPublicationService;
import com.editorial.publications.domain.entity.PublicationStatus;
import com.editorial.publications.presentation.dto.CreatePublicationDTO;
import com.editorial.publications.presentation.dto.PublicationResponseDTO;
import com.editorial.publications.presentation.dto.UpdatePublicationStatusDTO;
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
@RequestMapping("/api/v1/publications")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicationController {

    private final IPublicationService publicationService;

    @PostMapping
    public ResponseEntity<?> createPublication(@Valid @RequestBody CreatePublicationDTO createPublicationDTO) {
        log.info("POST /publications - Creating new publication: {}", createPublicationDTO.getTitle());
        try {
            PublicationResponseDTO response = publicationService.createPublication(createPublicationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating publication: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPublicationById(@PathVariable Long id) {
        log.info("GET /publications/{} - Fetching publication", id);
        try {
            PublicationResponseDTO response = publicationService.getPublicationById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching publication: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPublications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        log.info("GET /publications - Fetching all publications, page: {}, size: {}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            Page<PublicationResponseDTO> response = publicationService.getAllPublications(pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching publications: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<?> getPublicationsByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /publications/author/{} - Fetching publications for author", authorId);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PublicationResponseDTO> response = publicationService.getPublicationsByAuthor(authorId, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching publications by author: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getPublicationsByStatus(
            @PathVariable PublicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /publications/status/{} - Fetching publications with status", status);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PublicationResponseDTO> response = publicationService.getPublicationsByStatus(status, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching publications by status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPublications(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /publications/search - Searching publications with keyword: {}", keyword);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PublicationResponseDTO> response = publicationService.searchPublications(keyword, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching publications: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updatePublicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePublicationStatusDTO updateDTO) {
        log.info("PATCH /publications/{}/status - Updating status to: {}", id, updateDTO.getStatus());
        try {
            PublicationResponseDTO response = publicationService.updatePublicationStatus(id, updateDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating publication status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePublication(@PathVariable Long id) {
        log.info("DELETE /publications/{} - Deleting publication", id);
        try {
            publicationService.deletePublication(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting publication: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats/total")
    public ResponseEntity<?> getTotalPublications() {
        log.debug("GET /publications/stats/total - Getting total publications");
        try {
            long total = publicationService.getTotalPublications();
            Map<String, Object> response = new HashMap<>();
            response.put("total", total);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting total publications: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stats/by-status/{status}")
    public ResponseEntity<?> getTotalPublicationsByStatus(@PathVariable PublicationStatus status) {
        log.debug("GET /publications/stats/by-status/{} - Getting total by status", status);
        try {
            long total = publicationService.getTotalPublicationsByStatus(status);
            Map<String, Object> response = new HashMap<>();
            response.put("status", status);
            response.put("total", total);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting publications by status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
