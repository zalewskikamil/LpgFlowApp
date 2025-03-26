package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.util.OrderStatus;
import com.github.lpgflow.domain.warehouse.WarehouseFacade;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehousesByRegionalManagerEmailResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.WarehouseDto;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
class OrderRetriever {

    private final OrderRepository orderRepository;
    private final WarehouseFacade warehouseFacade;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    List<Order> getOrders(Pageable pageable) {
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        List<String> currentUserRoles = authenticatedUserProvider.getCurrentUserRoles();
        if (currentUserRoles.contains("ADMIN")  || currentUserRoles.contains("PLANNER")) {
            return orderRepository.findAll(pageable);
        }
        if (currentUserRoles.contains("REGIONAL_MANAGER")) {
            return getOrdersByRegionalManagerEmail(currentUserEmail, pageable);
        }
        if (currentUserRoles.contains("WAREHOUSEMAN")) {
            return getOrdersByWarehousemanEmail(currentUserEmail, pageable);
        }
        return List.of();
    }

    List<Order> getOrdersByRegionalManagerEmail(String regionalManagerEmail, Pageable pageable) {
        GetWarehousesByRegionalManagerEmailResponseDto facadeResponse =
                warehouseFacade.getWarehousesByRegionalManagerEmail(regionalManagerEmail);
        List<WarehouseDto> warehousesDto = facadeResponse.warehouses();
        List<String> warehouseNames = warehousesDto.stream()
                .map(WarehouseDto::name)
                .collect(Collectors.toList());
        return orderRepository.getOrdersByWarehousesNames(warehouseNames, pageable);
    }

    List<Order> getOrdersByWarehousemanEmail(String warehousemanEmail, Pageable pageable) {
        GetWarehouseResponseDto facadeResponse = warehouseFacade.findWarehouseByWarehousemanEmail(warehousemanEmail);
        String warehouseName = facadeResponse.warehouse().name();
        return getOrdersByWarehouseName(warehouseName, pageable);
    }

    List<Order> getOrdersByWarehouseName(String warehouseName, Pageable pageable) {
        return orderRepository.findByWarehouseName(warehouseName, pageable);
    }

    Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new OrderNotFoundException("Order with id: " + id + " not found"));
    }

    List<Order> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.getOrdersByStatus(status, pageable);
    }

    List<Order> getOrdersByCompletionDate(Instant date, Pageable pageable) {
        return orderRepository.getOrdersByScheduledCompletionDate(date, pageable);
    }

    List<Order> getOrdersByCompletionDateAndStatus(Instant date, OrderStatus status, Pageable pageable) {
        return orderRepository.getOrdersByScheduledCompletionDateAndStatus(date, status, pageable);
    }
}
