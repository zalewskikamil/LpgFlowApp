package com.github.lpgflow.domain.order;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(final String message) {
        super(message);
    }
}
