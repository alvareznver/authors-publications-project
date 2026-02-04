package com.editorial.authors.presentation.dto;

import com.editorial.authors.domain.entity.AuthorType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAuthorDTO {

    @NotBlank(message = "Author name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    private String bio;

    @NotNull(message = "Author type is required")
    private AuthorType authorType;

    private String country;

    private String phone;
}
