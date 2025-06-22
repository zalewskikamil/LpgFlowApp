package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.util.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryOrderRepository implements OrderRepository {

    Map<Long, Order> db = new HashMap<>();
    AtomicInteger index = new AtomicInteger(1);

    @Override
    public List<Order> getAllWithFilters(final OrderStatus status, final Instant from, final Instant to, final List<String> warehouses, final Pageable pageable) {
        return db.values().stream()
                .filter(order -> status == null || order.getStatus() == status)
                .filter(order -> from == null || order.getScheduledCompletionDate().compareTo(from) >= 0)
                .filter(order -> to == null || order.getScheduledCompletionDate().compareTo(to) <= 0)
                .filter(order -> warehouses == null || warehouses.contains(order.getWarehouseName()))
                .toList();
    }

    @Override
    public Optional<Order> findById(final Long id) {
        Order order = db.get(id);
        return Optional.ofNullable(order);
    }

    @Override
    public Order save(final Order order) {
        Long orderId = order.getId();
        if (orderId != null && db.containsKey(orderId)) {
            db.put(orderId, order);
        } else {
            long index = this.index.getAndIncrement();
            db.put(index, order);
            order.setId(index);
        }
        return order;
    }
}
