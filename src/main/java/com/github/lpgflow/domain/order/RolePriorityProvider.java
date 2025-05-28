package com.github.lpgflow.domain.order;

import java.util.List;
import java.util.Optional;

class RolePriorityProvider {

    private static final List<String> PRIORITY =
            List.of("PLANNER", "PRODUCTION_MANAGER", "REGIONAL_MANAGER", "WAREHOUSEMAN");

    static Optional<String> getHighestPriorityRole(List<String> userRoles) {
        return PRIORITY.stream()
                .filter(userRoles::contains)
                .findFirst();
    }
}
