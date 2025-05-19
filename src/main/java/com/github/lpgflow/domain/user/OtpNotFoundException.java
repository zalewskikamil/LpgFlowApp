package com.github.lpgflow.domain.user;

public class OtpNotFoundException extends RuntimeException {

    public OtpNotFoundException(String message) {
        super(message);
    }
}
