package com.github.lpgflow.domain.order;

import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class OrderRetriever {

    private final OrderRepository orderRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final OrderQueryStrategyRouter strategyRouter;

    List<Order> getOrders(OrderQueryCriteria criteria) {
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        List<String> currentUserRoles = authenticatedUserProvider.getCurrentUserRoles();
        return strategyRouter.getOrders(criteria, currentUserEmail, currentUserRoles);
    }

    Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order with id: " + id + " not found"));
    }
}
