package com.editorial.publications.infrastructure.client;

import com.editorial.publications.presentation.dto.PublicationResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Component
@Slf4j
public class AuthorServiceClient {

    @Value("${authors.service.url}")
    private String authorsServiceUrl;

    @Value("${authors.service.timeout:5000}")
    private long timeout;

    private final RestTemplate restTemplate;

    public AuthorServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AuthorInfo getAuthorById(Long authorId) {
        try {
            String url = authorsServiceUrl + "/api/v1/authors/" + authorId;
            log.debug("Calling Authors Service: {}", url);

            ResponseEntity<AuthorResponse> response = restTemplate.getForEntity(url, AuthorResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.debug("Author found with id: {}", authorId);
                return mapToAuthorInfo(response.getBody());
            }

            log.warn("Author not found with id: {}", authorId);
            return null;

        } catch (RestClientException e) {
            log.error("Error calling Authors Service for id {}: {}", authorId, e.getMessage());
            throw new AuthorServiceException("Failed to validate author: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error when calling Authors Service: {}", e.getMessage());
            throw new AuthorServiceException("Unexpected error validating author");
        }
    }

    public boolean authorExists(Long authorId) {
        try {
            String url = authorsServiceUrl + "/api/v1/authors/" + authorId + "/exists";
            log.debug("Checking author existence: {}", url);

            ResponseEntity<ExistsResponse> response = restTemplate.getForEntity(url, ExistsResponse.class);
            boolean exists = response.getBody() != null && response.getBody().exists;

            log.debug("Author existence check for id {}: {}", authorId, exists);
            return exists;

        } catch (RestClientException e) {
            log.error("Error checking author existence: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error checking author existence: {}", e.getMessage());
            return false;
        }
    }

    private AuthorInfo mapToAuthorInfo(AuthorResponse response) {
        return AuthorInfo.builder()
                .id(response.id)
                .name(response.name)
                .email(response.email)
                .authorType(response.authorType)
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorResponse {
        public Long id;
        public String name;
        public String email;
        public String authorType;
        public String country;
        public String phone;
        public Integer publicationsCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExistsResponse {
        public Long id;
        public boolean exists;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthorInfo {
        public Long id;
        public String name;
        public String email;
        public String authorType;
    }
}

class AuthorServiceException extends RuntimeException {
    public AuthorServiceException(String message) {
        super(message);
    }

    public AuthorServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
