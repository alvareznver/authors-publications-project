package com.editorial.authors.domain.entity;

public enum AuthorType {
    INDIVIDUAL("Individual"),
    ORGANIZATION("Organization"),
    ACADEMIC("Academic");

    private final String description;

    AuthorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
