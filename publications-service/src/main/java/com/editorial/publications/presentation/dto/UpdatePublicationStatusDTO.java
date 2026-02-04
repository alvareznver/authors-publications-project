package com.editorial.publications.presentation.dto;

import com.editorial.publications.domain.entity.PublicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePublicationStatusDTO {

    @NotNull(message = "Status is required")
    private PublicationStatus status;

    private String reviewerNotes;

    private String rejectionReason;
}
