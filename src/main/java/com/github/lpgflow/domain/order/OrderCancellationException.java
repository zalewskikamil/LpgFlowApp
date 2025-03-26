package com.github.lpgflow.domain.order;

public class OrderCancellationException extends RuntimeException {

    public OrderCancellationException(final String message) {
        super(message);
    }
}
