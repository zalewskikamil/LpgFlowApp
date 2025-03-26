package com.github.lpgflow.domain.warehouse;

public class AddressInUseException extends RuntimeException {

    public AddressInUseException(final String message) {
        super(message);
    }
}
