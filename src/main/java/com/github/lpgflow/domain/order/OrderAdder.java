package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.bdf.BdfFacade;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
class OrderAdder {

    private final OrderRepository orderRepository;
    private final OrderParametersValidator orderParametersValidator;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final BdfFacade bdfFacade;

    Order addOrder(Order order) {
        orderParametersValidator.validateForCreation(order);
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        String warehouseName = order.getWarehouseName();
        Instant completionDate = order.getScheduledCompletionDate();
        order.setCreatedBy(currentUserEmail);
        log.info("Adding new order by {}. Warehouse name {}, completion date {}",
                currentUserEmail, warehouseName, completionDate);
        order.getBdfIds().forEach(bdfId -> bdfFacade.setOrderedStatus(bdfId, true));
        return orderRepository.save(order);
    }
}
