package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.bdf.BdfFacade;
import com.github.lpgflow.domain.warehouse.WarehouseFacade;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;

import java.time.Clock;
import java.util.List;

class OrderFacadeConfiguration {

    public static OrderFacade createOrderFacade(final OrderRepository orderRepository,
                                                final AuthenticatedUserProvider authenticatedUserProvider,
                                                final WarehouseFacade warehouseFacade,
                                                final BdfFacade bdfFacade,
                                                final Clock clock) {
        OrderQueryStrategyRouter orderQueryStrategyRouter = getOrderQueryStrategyRouter(orderRepository, warehouseFacade);
        OrderRetriever orderRetriever = new OrderRetriever(orderRepository, authenticatedUserProvider, orderQueryStrategyRouter);
        OrderAccessValidator orderAccessValidator = new OrderAccessValidator(authenticatedUserProvider, warehouseFacade);
        OrderParametersValidator orderParametersValidator = new OrderParametersValidator(
                orderAccessValidator, authenticatedUserProvider, bdfFacade, warehouseFacade, clock);
        OrderAdder orderAdder = new OrderAdder(orderRepository, orderParametersValidator, authenticatedUserProvider, bdfFacade);
        OrderCanceler orderCanceler = new OrderCanceler(orderRetriever, orderRepository, bdfFacade, orderAccessValidator);
        return new OrderFacade(orderRetriever, orderAdder, orderCanceler);
    }

    private static OrderQueryStrategyRouter getOrderQueryStrategyRouter(final OrderRepository orderRepository,
                                                                        final WarehouseFacade warehouseFacade) {
        PlannerOrderQueryStrategy plannerOrderQueryStrategy = new PlannerOrderQueryStrategy(orderRepository);
        ProductionManagerOrderQueryStrategy productionManagerOrderQueryStrategy =
                new ProductionManagerOrderQueryStrategy(orderRepository);
        RegionalManagerOrderQueryStrategy regionalManagerOrderQueryStrategy =
                new RegionalManagerOrderQueryStrategy(orderRepository, warehouseFacade);
        WarehousemanOrderQueryStrategy warehousemanOrderQueryStrategy =
                new WarehousemanOrderQueryStrategy(orderRepository, warehouseFacade);
        List<OrderQueryStrategy> strategies = List.of(
                plannerOrderQueryStrategy,
                productionManagerOrderQueryStrategy,
                regionalManagerOrderQueryStrategy,
                warehousemanOrderQueryStrategy);
        return new OrderQueryStrategyRouter(strategies);
    }
}
