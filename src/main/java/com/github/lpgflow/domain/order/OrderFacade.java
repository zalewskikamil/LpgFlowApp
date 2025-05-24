package com.github.lpgflow.domain.order;


import com.github.lpgflow.domain.order.dto.request.CreateOrderRequestDto;
import com.github.lpgflow.domain.order.dto.response.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderRetriever orderRetriever;
    private final OrderAdder orderAdder;
    private final OrderCanceler orderCanceler;

    public List<OrderDto> getOrders(Pageable pageable) {
        List<Order> orders = orderRetriever.getOrders(pageable);
        return OrderMapper.mapFromListOrdersToListOrdersDto(orders);
    }

    public OrderDto findById(Long id) {
        Order orderById = orderRetriever.findById(id);
        return OrderMapper.mapFromOrderToOrderDto(orderById);
    }

    public OrderDto addOrder(CreateOrderRequestDto request) {
        Order order = OrderMapper.mapFromCreateOrderRequestDtoToOrder(request);
        Order savedOrder = orderAdder.addOrder(order);
        return OrderMapper.mapFromOrderToOrderDto(savedOrder);
    }

    public void cancelOrderById(Long id) {
        orderCanceler.cancelOrderById(id);
    }
}
