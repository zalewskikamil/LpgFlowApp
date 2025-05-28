package com.github.lpgflow.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class OrderQueryStrategyRouter {

    private final List<OrderQueryStrategy> strategies;

    List<Order> getOrders(OrderQueryCriteria criteria, String userEmail, List<String> userRoles) {
        String highestPriorityUserRole = RolePriorityProvider.getHighestPriorityRole(userRoles).get();
        OrderQueryStrategy strategyByRole = findStrategyByRole(highestPriorityUserRole);
        return strategyByRole.getOrders(criteria, userEmail);
    }

    private OrderQueryStrategy findStrategyByRole(String role) {
        return strategies.stream()
                .filter(strategy -> strategy.getSupportedRole().equals(role))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No strategy supports role: " + role));
    }
}
