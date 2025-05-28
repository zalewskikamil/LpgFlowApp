package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.util.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

interface OrderRepository extends Repository<Order, Long> {

    @Query("""
            SELECT o FROM Order o
            WHERE (:status IS NULL OR o.status = :status)
            AND (CAST(:from AS TIMESTAMP) IS NULL OR o.scheduledCompletionDate >= :from)
            AND (CAST(:to AS TIMESTAMP) IS NULL OR o.scheduledCompletionDate <= :to)
            AND (:warehouses IS NULL OR o.warehouseName IN :warehouses)
            """)
    List<Order> getAllWithFilters(OrderStatus status,
                                  Instant from,
                                  Instant to,
                                  List<String> warehouses,
                                  Pageable pageable);

    Optional<Order> findById(Long id);

    Order save(Order order);
}
