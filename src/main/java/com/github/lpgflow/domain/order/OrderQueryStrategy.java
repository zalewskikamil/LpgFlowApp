package com.github.lpgflow.domain.order;

import java.util.List;

interface OrderQueryStrategy {

    String getSupportedRole();

    List<Order> getOrders(OrderQueryCriteria criteria, String currentUserEmail);
}
