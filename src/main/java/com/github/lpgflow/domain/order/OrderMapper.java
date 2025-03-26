package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.order.dto.request.CreateOrderRequestDto;
import com.github.lpgflow.domain.order.dto.response.OrderDto;
import com.github.lpgflow.domain.util.OrderStatus;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

class OrderMapper {

    static Order mapFromCreateOrderRequestDtoToOrder(CreateOrderRequestDto dto) {
        String completionDateAsString = dto.scheduledCompletionDate();
        Instant completionDate;
        try {
            completionDate = DateConverter.toInstant(completionDateAsString);
        } catch (DateTimeParseException e) {
            throw new OrderParameterException("Invalid date");
        }
        return Order.builderWithBdfIds(dto.bdfIds())
                .scheduledCompletionDate(completionDate)
                .warehouseName(dto.warehouseName())
                .status(OrderStatus.NEW)
                .build();
    }

    static OrderDto mapFromOrderToOrderDto(Order order) {
        String completionDate = DateConverter.fromInstant(order.getScheduledCompletionDate());
        List<Long> bdfIds = order.getBdfIds().stream().toList();
        String status = order.getStatus().name();
        return OrderDto.builder()
                .id(order.getId())
                .bdfIds(bdfIds)
                .createdBy(order.getCreatedBy())
                .completionDate(completionDate)
                .warehouseName(order.getWarehouseName())
                .status(status)
                .build();
    }

    static List<OrderDto> mapFromListOrdersToListOrdersDto(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::mapFromOrderToOrderDto)
                .collect(Collectors.toList());
    }
}
