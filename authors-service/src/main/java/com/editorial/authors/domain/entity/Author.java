package com.editorial.authors.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author extends BaseEntity {

    @NotBlank(message = "Author name cannot be blank")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "author_type", nullable = false)
    private AuthorType authorType;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "publications_count")
    private Integer publicationsCount = 0;

    @Override
    public String getDisplayName() {
        return this.name + " (" + this.authorType.getDescription() + ")";
    }

    public void incrementPublicationsCount() {
        if (this.publicationsCount == null) {
            this.publicationsCount = 0;
        }
        this.publicationsCount++;
    }

    public void decrementPublicationsCount() {
        if (this.publicationsCount != null && this.publicationsCount > 0) {
            this.publicationsCount--;
        }
    }
}
