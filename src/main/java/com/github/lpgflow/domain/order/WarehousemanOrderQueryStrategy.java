package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.warehouse.WarehouseFacade;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehouseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class WarehousemanOrderQueryStrategy implements OrderQueryStrategy {

    private final OrderRepository orderRepository;
    private final WarehouseFacade warehouseFacade;

    @Override
    public String getSupportedRole() {
        return "WAREHOUSEMAN";
    }

    @Override
    public List<Order> getOrders(OrderQueryCriteria criteria, String currentUserEmail) {
        GetWarehouseResponseDto facadeResponse = warehouseFacade.findWarehouseByWarehousemanEmail(currentUserEmail);
        String warehouseName = facadeResponse.warehouse().name();
        return orderRepository.getAllWithFilters(
                criteria.status().orElse(null),
                criteria.from().orElse(null),
                criteria.to().orElse(null),
                List.of(warehouseName),
                criteria.pageable());
    }
}
