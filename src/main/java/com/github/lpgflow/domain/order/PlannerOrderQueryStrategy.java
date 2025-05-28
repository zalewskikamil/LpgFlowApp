package com.github.lpgflow.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class PlannerOrderQueryStrategy implements OrderQueryStrategy {

    private final OrderRepository orderRepository;

    @Override
    public String getSupportedRole() {
        return "PLANNER";
    }

    @Override
    public List<Order> getOrders(OrderQueryCriteria criteria, String currentUserEmail) {
        return orderRepository.getAllWithFilters(
                criteria.status().orElse(null),
                criteria.from().orElse(null),
                criteria.to().orElse(null),
                criteria.warehouseNames().orElse(null),
                criteria.pageable());
    }
}
