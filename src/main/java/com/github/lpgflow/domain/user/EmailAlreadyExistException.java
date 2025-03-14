package com.github.lpgflow.domain.user;

public class EmailAlreadyExistException extends RuntimeException {

    public EmailAlreadyExistException(final String message) {
        super(message);
    }
}
