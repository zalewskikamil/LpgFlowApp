package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.util.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public record OrderQueryCriteria(
        Optional<OrderStatus> status,
        Optional<Instant> from,
        Optional<Instant> to,
        Optional<List<String>> warehouseNames,
        Pageable pageable
) {
}
