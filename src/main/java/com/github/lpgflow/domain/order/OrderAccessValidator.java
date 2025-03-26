package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.warehouse.WarehouseFacade;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehouseResponseDto;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
class OrderAccessValidator {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final WarehouseFacade warehouseFacade;

    boolean hasAccess(String warehouseName) {
        return hasAccessAsPlanner() || hasAccessAsRegionalManager(warehouseName) || hasAccessAsWarehouseman(warehouseName);
    }

    private boolean hasAccessAsPlanner() {
        return authenticatedUserProvider.getCurrentUserRoles().stream()
                .anyMatch(role -> Objects.equals(role, "PLANNER"));
    }

    private boolean hasAccessAsRegionalManager(String warehouseName) {
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        GetWarehouseResponseDto warehouseFacadeResponse = warehouseFacade.findWarehouseByName(warehouseName);
        return warehouseFacadeResponse.warehouse().regionalManagerEmail().equals(currentUserEmail);
    }

    private boolean hasAccessAsWarehouseman(String warehouseName) {
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        GetWarehouseResponseDto warehouseFacadeResponse = warehouseFacade.findWarehouseByName(warehouseName);
        return warehouseFacadeResponse.warehouse().warehousemanEmail().equals(currentUserEmail);
    }
}
