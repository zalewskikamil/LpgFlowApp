package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.warehouse.WarehouseFacade;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehousesByRegionalManagerEmailResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.WarehouseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class RegionalManagerOrderQueryStrategy implements OrderQueryStrategy {

    private final OrderRepository orderRepository;
    private final WarehouseFacade warehouseFacade;

    @Override
    public String getSupportedRole() {
        return "REGIONAL_MANAGER";
    }

    @Override
    public List<Order> getOrders(OrderQueryCriteria criteria, String currentUserEmail) {
        GetWarehousesByRegionalManagerEmailResponseDto facadeResponse =
                warehouseFacade.getWarehousesByRegionalManagerEmail(currentUserEmail);
        List<String> managedWarehouseNames = facadeResponse.warehouses()
                .stream()
                .map(WarehouseDto::name)
                .toList();
        List<String> filteredWarehouseNames = criteria.warehouseNames()
                .map(requestedNames ->
                        managedWarehouseNames.stream()
                                .filter(requestedNames::contains)
                                .toList())
                .orElse(managedWarehouseNames);
        return orderRepository.getAllWithFilters(
                criteria.status().orElse(null),
                criteria.from().orElse(null),
                criteria.to().orElse(null),
                filteredWarehouseNames,
                criteria.pageable());
    }
}
