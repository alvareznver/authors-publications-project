package com.editorial.authors.infrastructure.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

class InvalidAuthorDataException extends RuntimeException {
    public InvalidAuthorDataException(String message) {
        super(message);
    }

    public InvalidAuthorDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
