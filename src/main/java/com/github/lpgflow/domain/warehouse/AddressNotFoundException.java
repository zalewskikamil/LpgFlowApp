package com.github.lpgflow.domain.warehouse;

public class AddressNotFoundException extends RuntimeException {

    public AddressNotFoundException(final String message) {
        super(message);
    }
}
