package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.util.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

interface OrderRepository extends Repository<Order, Long> {


    List<Order> findAll(Pageable pageable);

    Optional<Order> findById(Long id);

    @Query("SELECT o FROM Order o WHERE o.warehouseName = :warehuseName")
    List<Order> findByWarehouseName(String warehouseName, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.warehouseName IN :warehusesNames")
    List<Order> getOrdersByWarehousesNames(List<String> warehousesNames, Pageable pageable);

    List<Order> getOrdersByStatus(OrderStatus status, Pageable pageable);

    List<Order> getOrdersByScheduledCompletionDate(Instant date, Pageable pageable);

    List<Order> getOrdersByScheduledCompletionDateAndStatus(Instant date, OrderStatus status, Pageable pageable);

    Order save(Order order);

}
