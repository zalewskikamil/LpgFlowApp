package com.github.lpgflow.domain.warehouse;

public class WarehouseNotFoundException extends RuntimeException {

    public WarehouseNotFoundException(final String message) {
        super(message);
    }
}
