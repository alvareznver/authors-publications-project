package com.editorial.publications.domain.entity;

public enum PublicationStatus {
    DRAFT("Draft - Being prepared"),
    IN_REVIEW("In Review - Under editorial review"),
    APPROVED("Approved - Ready to publish"),
    PUBLISHED("Published - Available to readers"),
    REJECTED("Rejected - Not accepted"),
    ARCHIVED("Archived - No longer active");

    private final String description;

    PublicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(PublicationStatus targetStatus) {
        return switch(this) {
            case DRAFT -> targetStatus == IN_REVIEW || targetStatus == REJECTED;
            case IN_REVIEW -> targetStatus == APPROVED || targetStatus == REJECTED || targetStatus == DRAFT;
            case APPROVED -> targetStatus == PUBLISHED || targetStatus == DRAFT;
            case PUBLISHED -> targetStatus == ARCHIVED;
            case REJECTED -> targetStatus == DRAFT;
            case ARCHIVED -> false;
        };
    }
}
