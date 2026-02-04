package com.editorial.publications.infrastructure.exception;

public class PublicationNotFoundException extends RuntimeException {
    public PublicationNotFoundException(String message) {
        super(message);
    }

    public PublicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

class PublicationValidationException extends RuntimeException {
    public PublicationValidationException(String message) {
        super(message);
    }

    public PublicationValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

class AuthorValidationException extends RuntimeException {
    public AuthorValidationException(String message) {
        super(message);
    }

    public AuthorValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
