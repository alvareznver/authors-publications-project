package com.editorial.publications.presentation.dto;

import com.editorial.publications.domain.entity.PublicationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicationResponseDTO {

    private Long id;

    private String title;

    private String description;

    private String content;

    private Long authorId;

    private AuthorDTO author;

    private PublicationStatus status;

    private String keywords;

    private String category;

    private String language;

    private Integer viewsCount;

    private String reviewerNotes;

    private String rejectionReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedAt;

    private Boolean isActive;

    private String summary;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthorDTO {
        private Long id;
        private String name;
        private String email;
        private String authorType;
    }
}
