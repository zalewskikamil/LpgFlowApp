package com.github.lpgflow.domain.order;


import com.github.lpgflow.domain.order.dto.request.CreateOrderRequestDto;
import com.github.lpgflow.domain.order.dto.request.GetOrdersRequestDto;
import com.github.lpgflow.domain.order.dto.response.CreateOrderResponseDto;
import com.github.lpgflow.domain.order.dto.response.GetAllOrdersResponse;
import com.github.lpgflow.domain.order.dto.response.GetOrderResponseDto;
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

    public GetAllOrdersResponse getOrders(GetOrdersRequestDto request, Pageable pageable) {
        OrderQueryCriteria criteria = OrderMapper
                .mapFromGetOrdersRequestDtoToOrderQueryCriteria(request, pageable);
        List<Order> orders = orderRetriever.getOrders(criteria);
        return OrderMapper.mapFromListOrdersToGetAllOrdersResponse(orders);
    }

    public GetOrderResponseDto findById(Long id) {
        Order orderById = orderRetriever.findById(id);
        return OrderMapper.mapFromOrderToGetOrderResponseDto(orderById);
    }

    public CreateOrderResponseDto addOrder(CreateOrderRequestDto request) {
        Order order = OrderMapper.mapFromCreateOrderRequestDtoToOrder(request);
        Order savedOrder = orderAdder.addOrder(order);
        return OrderMapper.mapFromOrderToCreateOrderResponseDto(savedOrder);
    }

    public void cancelOrderById(Long id) {
        orderCanceler.cancelOrderById(id);
    }
}
