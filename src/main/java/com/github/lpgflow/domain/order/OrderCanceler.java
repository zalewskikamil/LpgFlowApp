package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.bdf.BdfFacade;
import com.github.lpgflow.domain.util.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
class OrderCanceler {

    private final OrderRetriever orderRetriever;
    private final OrderRepository orderRepository;
    private final BdfFacade bdfFacade;
    private final OrderAccessValidator accessValidator;

    void cancelOrderById(Long orderId) {
        Order orderById = orderRetriever.findById(orderId);
        String warehouseName = orderById.getWarehouseName();
        if (!accessValidator.hasAccess(warehouseName)) {
            throw new OrderAccessException("No access to order with id: " + orderId);
        }
        OrderStatus status = orderById.getStatus();
        if (!status.canTransitionTo(OrderStatus.CANCELED)) {
            throw new OrderCancellationException("Order status not allowed to cancellation");
        }
        orderById.setStatus(OrderStatus.CANCELED);
        orderRepository.save(orderById);
        Collection<Long> bdfsFromOrder = orderById.getBdfIds();
        for (Long bdfId : bdfsFromOrder) {
            bdfFacade.setOrderedStatus(bdfId, false);
        }
    }
}
