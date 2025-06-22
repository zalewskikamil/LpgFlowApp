package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.order.dto.request.CreateOrderRequestDto;
import com.github.lpgflow.domain.order.dto.request.GetOrdersRequestDto;
import com.github.lpgflow.domain.order.dto.response.CreateOrderResponseDto;
import com.github.lpgflow.domain.order.dto.response.GetAllOrdersResponse;
import com.github.lpgflow.domain.order.dto.response.GetOrderResponseDto;
import com.github.lpgflow.domain.order.dto.response.OrderDto;
import com.github.lpgflow.domain.util.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
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

    static OrderQueryCriteria mapFromGetOrdersRequestDtoToOrderQueryCriteria(GetOrdersRequestDto dto,
                                                                             Pageable pageable) {
        Optional<OrderStatus> orderStatus = Optional.ofNullable(dto.orderStatus()).map(OrderStatus::valueOf);
        Optional<Instant> from = Optional.ofNullable(dto.from()).map(DateConverter::toInstant);
        Optional<Instant> to = Optional.ofNullable(dto.to()).map(DateConverter::toInstant);
        if (from.isPresent() && to.isPresent()) {
            verifyDateRange(from.get(), to.get());
        }
        Optional<List<String>> warehouseNames =
                Optional.ofNullable(dto.warehouseNames())
                        .filter(list -> !list.isEmpty());
        return new OrderQueryCriteria(orderStatus, from, to, warehouseNames, pageable);
    }

    private static void verifyDateRange(Instant from, Instant to) {
        if (from.isAfter(to)) {
            throw new OrderParameterException("Invalid date range");
        }
    }

    static GetAllOrdersResponse mapFromListOrdersToGetAllOrdersResponse(List<Order> orders) {
        return GetAllOrdersResponse.builder()
                .orders(orders.stream()
                        .map(OrderMapper::mapFromOrderToOrderDto)
                        .collect(Collectors.toList()))
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

    static GetOrderResponseDto mapFromOrderToGetOrderResponseDto(Order order) {
        return GetOrderResponseDto.builder()
                .order(mapFromOrderToOrderDto(order))
                .build();
    }

    static CreateOrderResponseDto mapFromOrderToCreateOrderResponseDto(Order order) {
        return CreateOrderResponseDto.builder()
                .order(mapFromOrderToOrderDto(order))
                .build();
    }
}
