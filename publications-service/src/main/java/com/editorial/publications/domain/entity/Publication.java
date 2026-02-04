package com.editorial.publications.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "publications", indexes = {
        @Index(name = "idx_author_id", columnList = "author_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publication extends BasePublication {

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @NotNull(message = "Author ID is required")
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PublicationStatus status = PublicationStatus.DRAFT;

    @Column(name = "keywords", length = 500)
    private String keywords;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "language", length = 50)
    private String language = "ES";

    @Column(name = "views_count")
    private Integer viewsCount = 0;

    @Column(name = "reviewer_notes", length = 1000)
    private String reviewerNotes;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    @Override
    public String getSummary() {
        return String.format("%s (Status: %s, Author ID: %d)", 
                this.title, this.status.getDescription(), this.authorId);
    }

    public void updateStatus(PublicationStatus newStatus) {
        if (this.status.canTransitionTo(newStatus)) {
            this.status = newStatus;
            if (newStatus == PublicationStatus.PUBLISHED) {
                this.publishedAt = java.time.LocalDateTime.now();
            }
        } else {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s", this.status, newStatus)
            );
        }
    }

    public void incrementViewCount() {
        if (this.viewsCount == null) {
            this.viewsCount = 0;
        }
        this.viewsCount++;
    }
}
