package com.github.lpgflow.domain.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.lpgflow.domain.order.OrderParameterException;
import com.github.lpgflow.domain.warehouse.WarehouseParameterException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum OrderStatus {
    NEW,
    ACCEPTED,
    CHANGED,
    SENT_TO_PRODUCTION,
    IN_PRODUCTION,
    READY_FOR_SHIPMENT,
    IN_TRANSPORT,
    DELIVERED,
    CANCELED,
    FAILED;

    private static final Map<OrderStatus, Set<OrderStatus>> allowedTransitions = new HashMap<>();

     static {
        allowedTransitions.put(NEW, (Set.of(ACCEPTED, CHANGED, CANCELED)));
        allowedTransitions.put(ACCEPTED, (Set.of(CHANGED, SENT_TO_PRODUCTION, CANCELED)));
        allowedTransitions.put(CHANGED, (Set.of(ACCEPTED, SENT_TO_PRODUCTION, CANCELED)));
        allowedTransitions.put(SENT_TO_PRODUCTION, (Set.of(IN_PRODUCTION, CANCELED)));
        allowedTransitions.put(IN_PRODUCTION, (Set.of(READY_FOR_SHIPMENT, FAILED)));
        allowedTransitions.put(READY_FOR_SHIPMENT, (Set.of(IN_TRANSPORT, FAILED)));
        allowedTransitions.put(IN_TRANSPORT, (Set.of(DELIVERED, FAILED)));
        allowedTransitions.put(DELIVERED, (Set.of()));
        allowedTransitions.put(CANCELED, (Set.of()));
        allowedTransitions.put(FAILED, (Set.of()));
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        return allowedTransitions.getOrDefault(this, Set.of()).contains(newStatus);
    }

    @JsonCreator
    public static OrderStatus fromString(String value) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.name().equalsIgnoreCase(value)) {
                return orderStatus;
            }
        }
        throw new OrderParameterException("Illegal value of order status");
    }
}
