package com.github.lpgflow.domain.order;

import com.github.lpgflow.domain.bdf.BdfFacade;
import com.github.lpgflow.domain.bdf.dto.response.BdfDto;
import com.github.lpgflow.domain.bdf.dto.response.GetBdfByIdDto;
import com.github.lpgflow.domain.order.dto.request.CreateOrderRequestDto;
import com.github.lpgflow.domain.order.dto.request.GetOrdersRequestDto;
import com.github.lpgflow.domain.order.dto.response.CreateOrderResponseDto;
import com.github.lpgflow.domain.order.dto.response.GetAllOrdersResponse;
import com.github.lpgflow.domain.order.dto.response.GetOrderResponseDto;
import com.github.lpgflow.domain.order.dto.response.OrderDto;
import com.github.lpgflow.domain.util.enums.OrderStatus;
import com.github.lpgflow.domain.warehouse.WarehouseFacade;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehousesByRegionalManagerEmailResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.WarehouseDto;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class OrderFacadeTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2025-06-01T00:00:00Z");
    private static final Clock fixedClock = Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);
    private static final String NEW_ORDER_STATUS = OrderStatus.NEW.name();
    private static final String PLANNER_EMAIL = "planner@test.pl";
    private static final String PLANNER_ROLE_NAME = "PLANNER";
    private static final GetOrdersRequestDto DEFAULT_GET_REQUEST_DTO = GetOrdersRequestDto.builder().build();
    private static final String LARGE_BDF_SIZE = "LARGE";

    AuthenticatedUserProvider authenticatedUserProvider = mock(AuthenticatedUserProvider.class);
    WarehouseFacade warehouseFacade = mock(WarehouseFacade.class);
    BdfFacade bdfFacade = mock(BdfFacade.class);

    OrderFacade orderFacade = OrderFacadeConfiguration.createOrderFacade(
            new InMemoryOrderRepository(),
            authenticatedUserProvider,
            warehouseFacade,
            bdfFacade,
            fixedClock
    );

    @Test
    @DisplayName("Should return 2 orders When planner sent request without filters")
    public void should_return_two_orders_when_planner_sent_request_without_filters() {
        // given
        assertThatOrderDbIsEmpty();
        long bdf1Id = 1L;
        mockBdfFacadeGetBdfById(bdf1Id, LARGE_BDF_SIZE, 420, false, PLANNER_EMAIL);
        String warehouse1Name = "WH1";
        mockWarehouseFacadeFindWarehouseByName(warehouse1Name, LARGE_BDF_SIZE);
        String completionDate1 = "15-06-2025";
        CreateOrderRequestDto request1 = createOrderRequestDto(Set.of(bdf1Id), completionDate1, warehouse1Name);
        CreateOrderResponseDto createOrder1ResponseDto = orderFacade.addOrder(request1);
        Long order1Id = createOrder1ResponseDto.order().id();
        long bdf2Id = 2L;
        mockBdfFacadeGetBdfById(bdf2Id, "MEDIUM", 406, false, PLANNER_EMAIL);
        long bdf3Id = 3L;
        mockBdfFacadeGetBdfById(bdf3Id, "MEDIUM", 406, false, PLANNER_EMAIL);
        String warehouse2Name = "WH2";
        mockWarehouseFacadeFindWarehouseByName(warehouse2Name, "MEDIUM");
        String completionDate2 = "10-06-2025";
        CreateOrderRequestDto request2 = createOrderRequestDto(Set.of(bdf2Id, bdf3Id), completionDate2, warehouse2Name);
        CreateOrderResponseDto createOrder2ResponseDto = orderFacade.addOrder(request2);
        Long order2Id = createOrder2ResponseDto.order().id();
        // when
        List<OrderDto> result = orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders();
        // then
        OrderDto order1Dto = createOrderDto(
                order1Id, List.of(bdf1Id), PLANNER_EMAIL, completionDate1, warehouse1Name, NEW_ORDER_STATUS);
        OrderDto order2Dto = createOrderDto(
                order2Id, List.of(bdf2Id, bdf3Id), PLANNER_EMAIL, completionDate2, warehouse2Name, NEW_ORDER_STATUS);
        List<OrderDto> orders = List.of(order1Dto, order2Dto);
        assertThat(result).isEqualTo(orders);
    }

    private void assertThatOrderDbIsEmpty() {
        mockAuthenticatedUserProvider(PLANNER_EMAIL, PLANNER_ROLE_NAME);
        assertThat(orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders()).isEmpty();
    }

    private void mockAuthenticatedUserProvider(String userEmail, String userRoleName) {
        when(authenticatedUserProvider.getCurrentUserName()).thenReturn(userEmail);
        when(authenticatedUserProvider.getCurrentUserRoles()).thenReturn(List.of(userRoleName));
    }

    private void mockBdfFacadeGetBdfById(Long bdfId, String bdfSize, int bdfSlots, boolean ordered, String createdBy) {
        when(bdfFacade.getBdfById(bdfId)).thenReturn(GetBdfByIdDto.builder()
                .bdf(createBdfDto(bdfId, bdfSize, bdfSlots, ordered, createdBy))
                .build());
    }

    private BdfDto createBdfDto(Long bdfId, String bdfSize, int bdfSlots, boolean ordered, String createdBy) {
        return BdfDto.builder()
                .id(bdfId)
                .size(bdfSize)
                .slots(bdfSlots)
                .ordered(ordered)
                .createdBy(createdBy)
                .build();
    }

    private void mockWarehouseFacadeFindWarehouseByName(String warehouseName, String bdfSize) {
        when(warehouseFacade.findWarehouseByName(warehouseName)).thenReturn(
                GetWarehouseResponseDto.builder()
                        .warehouse(createWarehouseDto(warehouseName, bdfSize))
                        .build());
    }

    private WarehouseDto createWarehouseDto(String warehouseName, String assignedBdfSize) {
        return WarehouseDto.builder()
                .name(warehouseName)
                .bdfSize(assignedBdfSize)
                .active(true)
                .build();
    }

    private CreateOrderRequestDto createOrderRequestDto(Set<Long> bdfIds, String completionDate, String warehouseName) {
        return CreateOrderRequestDto.builder()
                .bdfIds(bdfIds)
                .scheduledCompletionDate(completionDate)
                .warehouseName(warehouseName)
                .build();
    }

    private OrderDto createOrderDto(long orderId, List<Long> bdfIds, String createdBy, String completionDate,
                                    String warehouseName, String status) {
        return OrderDto.builder()
                .id(orderId)
                .bdfIds(bdfIds)
                .createdBy(createdBy)
                .completionDate(completionDate)
                .warehouseName(warehouseName)
                .status(status)
                .build();
    }

    @Test
    @DisplayName("Should return 1 matching order When regional manager sent request with filters")
    public void should_return_one_matching_order_when_regional_manager_sent_request_with_filters() {
        // given
        assertThatOrderDbIsEmpty();
        long bdf1Id = 1L;
        mockBdfFacadeGetBdfById(bdf1Id, LARGE_BDF_SIZE, 420, false, PLANNER_EMAIL);
        String warehouse1Name = "WH1";
        mockWarehouseFacadeFindWarehouseByName(warehouse1Name, LARGE_BDF_SIZE);
        String completionDate1 = "10-06-2025";
        CreateOrderRequestDto request1 = createOrderRequestDto(Set.of(bdf1Id), completionDate1, warehouse1Name);
        CreateOrderResponseDto createOrder1ResponseDto = orderFacade.addOrder(request1);
        Long order1Id = createOrder1ResponseDto.order().id();
        long bdf2Id = 2L;
        mockBdfFacadeGetBdfById(bdf2Id, "MEDIUM", 406, false, PLANNER_EMAIL);
        long bdf3Id = 3L;
        mockBdfFacadeGetBdfById(bdf3Id, "MEDIUM", 406, false, PLANNER_EMAIL);
        String warehouse2Name = "WH2";
        mockWarehouseFacadeFindWarehouseByName(warehouse2Name, "MEDIUM");
        String completionDate2 = "12-06-2025";
        CreateOrderRequestDto request2 = createOrderRequestDto(Set.of(bdf2Id, bdf3Id), completionDate2, warehouse2Name);
        orderFacade.addOrder(request2);
        long bdf4Id = 4L;
        mockBdfFacadeGetBdfById(bdf4Id, "LARGE", 420, false, PLANNER_EMAIL);
        String completionDate3 = "20-06-2025";
        CreateOrderRequestDto request3 = createOrderRequestDto(Set.of(bdf4Id), completionDate3, warehouse1Name);
        orderFacade.addOrder(request3);
        assertThat(orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders()).hasSize(3);
        String regionalManagerEmail = "manager@test.pl";
        mockAuthenticatedUserProvider(regionalManagerEmail, "REGIONAL_MANAGER");
        mockWarehouseFacadeGetWarehousesByRegionalManagerEmail(List.of(warehouse1Name, warehouse2Name));
        String fromFilter = "05-06-2025";
        String toFilter = "15-06-2025";
        GetOrdersRequestDto getOrdersRequestDto = createGetOrdersRequestDto(fromFilter, toFilter,
                List.of(warehouse1Name));
        // when
        GetAllOrdersResponse result = orderFacade.getOrders(getOrdersRequestDto, Pageable.unpaged());
        //then
        OrderDto orderDto = createOrderDto(order1Id, List.of(bdf1Id), PLANNER_EMAIL, completionDate1,
                warehouse1Name, NEW_ORDER_STATUS);
        assertThat(result).isEqualTo(GetAllOrdersResponse.builder()
                .orders(List.of(orderDto))
                .build());
    }

    private GetOrdersRequestDto createGetOrdersRequestDto(String from, String to, List<String> warehouses) {
        return GetOrdersRequestDto.builder()
                .from(from)
                .to(to)
                .warehouseNames(warehouses)
                .build();
    }

    private void mockWarehouseFacadeGetWarehousesByRegionalManagerEmail(List<String> warehousesNames) {
        List<WarehouseDto> warehouseDtos = warehousesNames.stream()
                .map(warehousesName -> WarehouseDto.builder()
                        .name(warehousesName)
                        .build())
                .toList();
        when(warehouseFacade.getWarehousesByRegionalManagerEmail(anyString()))
                .thenReturn(GetWarehousesByRegionalManagerEmailResponseDto.builder()
                        .warehouses(warehouseDtos)
                        .build());
    }

    @Test
    @DisplayName("Should return order by id When id was exist in db")
    public void should_return_order_by_id_when_id_was_exist_in_db() {
        // given
        assertThatOrderDbIsEmpty();
        long bdfId = 1L;
        String completionDate = "15-06-2025";
        String warehouseName = "WH";
        CreateOrderRequestDto request = createOrderRequestDto(Set.of(bdfId), completionDate, warehouseName);
        long orderId = 1L;
        mockBdfFacadeGetBdfById(bdfId, LARGE_BDF_SIZE, 420, false, PLANNER_EMAIL);
        mockWarehouseFacadeFindWarehouseByName(warehouseName, LARGE_BDF_SIZE);
        orderFacade.addOrder(request);
        assertThat(orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders()).hasSize(1);
        // when
        GetOrderResponseDto response = orderFacade.findById(orderId);
        // then
        assertThat(response).isEqualTo(GetOrderResponseDto.builder()
                .order(createOrderDto(orderId, List.of(bdfId), PLANNER_EMAIL, completionDate,
                        warehouseName, OrderStatus.NEW.name()))
                .build());
    }

    @Test
    @DisplayName("Should throw exception OrderNotFound When id was not exist in db")
    public void should_throw_exception_order_not_found_when_id_was_not_exist_in_db() {
        // given
        assertThatOrderDbIsEmpty();
        long orderId = 1L;
        // when
        Throwable throwable = catchThrowable(() -> orderFacade.findById(orderId));
        // then
        assertThat(throwable).isInstanceOf(OrderNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("Order with id: " + orderId + " not found");
    }

    @Test
    @DisplayName("Should add order When planner sent request")
    public void should_add_order_when_planner_sent_request() {
        // given
        assertThatOrderDbIsEmpty();
        long bdfId = 1L;
        mockBdfFacadeGetBdfById(bdfId, LARGE_BDF_SIZE, 420, false, PLANNER_EMAIL);
        String warehouseName = "WH";
        mockWarehouseFacadeFindWarehouseByName(warehouseName, LARGE_BDF_SIZE);
        String completionDate = "15-06-2025";
        CreateOrderRequestDto request = createOrderRequestDto(Set.of(bdfId), completionDate, warehouseName);
        // when
        CreateOrderResponseDto result = orderFacade.addOrder(request);
        // then
        assertThat(result).isEqualTo(CreateOrderResponseDto.builder()
                .order(createOrderDto(1L, List.of(bdfId), PLANNER_EMAIL, completionDate,
                        warehouseName, NEW_ORDER_STATUS))
                .build());
        assertThat(orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders()).hasSize(1);
    }

    @Test
    @DisplayName("Should add order When regional_manager sent request")
    public void should_add_order_when_regional_manager_sent_request() {
        // given
        assertThatOrderDbIsEmpty();
        String regionalManagerEmail = "manager@test.pl";
        mockAuthenticatedUserProvider(regionalManagerEmail, "REGIONAL_MANAGER");
        long bdfId = 1L;
        mockBdfFacadeGetBdfById(bdfId, LARGE_BDF_SIZE, 420, false, regionalManagerEmail);
        String warehouseName = "WH";
        mockWarehouseFacadeFindWarehouseByName(warehouseName, LARGE_BDF_SIZE, regionalManagerEmail);
        String completionDate = "20-06-2025";
        CreateOrderRequestDto request = createOrderRequestDto(Set.of(bdfId), completionDate, warehouseName);
        // when
        CreateOrderResponseDto result = orderFacade.addOrder(request);
        // then
        assertThat(result).isEqualTo(CreateOrderResponseDto.builder()
                .order(createOrderDto(1L, List.of(bdfId), regionalManagerEmail, completionDate,
                        warehouseName, NEW_ORDER_STATUS))
                .build());
        mockAuthenticatedUserProvider(PLANNER_EMAIL, PLANNER_ROLE_NAME);
        assertThat(orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders()).hasSize(1);
    }


    private void mockWarehouseFacadeFindWarehouseByName(String warehouseName, String bdfSize, String regionalManagerEmail) {
        when(warehouseFacade.findWarehouseByName(warehouseName)).thenReturn(
                GetWarehouseResponseDto.builder()
                        .warehouse(createWarehouseDto(warehouseName, bdfSize, regionalManagerEmail))
                        .build());
    }

    private WarehouseDto createWarehouseDto(String warehouseName, String assignedBdfSize, String regionalManagerEmail) {
        return WarehouseDto.builder()
                .name(warehouseName)
                .regionalManagerEmail(regionalManagerEmail)
                .bdfSize(assignedBdfSize)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should add order When warehouseman sent request")
    public void should_add_order_when_warehouseman_sent_request() {
        // given
        assertThatOrderDbIsEmpty();
        String warehousemanEmail = "warehouseman@test.pl";
        mockAuthenticatedUserProvider(warehousemanEmail, "WAREHOUSEMAN");
        String warehouseName = "WH";
        mockWarehouseFacadeFindWarehouseByName(warehouseName, LARGE_BDF_SIZE, "manager@test.pl",
                warehousemanEmail);
        long bdfId = 1L;
        mockBdfFacadeGetBdfById(bdfId, LARGE_BDF_SIZE, 420, false, warehousemanEmail);
        String completionDate = "25-06-2025";
        CreateOrderRequestDto request = createOrderRequestDto(Set.of(bdfId), completionDate, warehouseName);
        // when
        CreateOrderResponseDto result = orderFacade.addOrder(request);
        // then
        assertThat(result).isEqualTo(CreateOrderResponseDto.builder()
                .order(createOrderDto(1L, List.of(bdfId), warehousemanEmail, completionDate,
                        warehouseName, NEW_ORDER_STATUS))
                .build());
        mockAuthenticatedUserProvider(PLANNER_EMAIL, PLANNER_ROLE_NAME);
        assertThat(orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders()).hasSize(1);
    }

    private void mockWarehouseFacadeFindWarehouseByName(String warehouseName, String bdfSize,
                                                        String regionalManagerEmail, String warehousemanEmail) {
        when(warehouseFacade.findWarehouseByName(warehouseName)).thenReturn(
                GetWarehouseResponseDto.builder()
                        .warehouse(createWarehouseDto(warehouseName, bdfSize, regionalManagerEmail, warehousemanEmail))
                        .build());
    }

    private WarehouseDto createWarehouseDto(String warehouseName, String assignedBdfSize,
                                            String regionalManagerEmail, String warehousemanEmail) {
        return WarehouseDto.builder()
                .name(warehouseName)
                .regionalManagerEmail(regionalManagerEmail)
                .warehousemanEmail(warehousemanEmail)
                .bdfSize(assignedBdfSize)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should cancel order by id When id was exist in db")
    public void should_cancel_order_by_id_when_id_was_exist_in_db() {
        // given
        assertThatOrderDbIsEmpty();
        String warehouseName = "WH";
        mockWarehouseFacadeFindWarehouseByName(warehouseName, LARGE_BDF_SIZE);
        String completionDate = "15-06-2025";
        long bdfId = 1L;
        mockBdfFacadeGetBdfById(bdfId, LARGE_BDF_SIZE, 420, false, PLANNER_EMAIL);
        long orderId = 1L;
        CreateOrderRequestDto request = createOrderRequestDto(Set.of(orderId), completionDate, warehouseName);
        orderFacade.addOrder(request);
        assertThat(orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders()).hasSize(1);
        // when
        orderFacade.cancelOrderById(orderId);
        // then
        GetOrderResponseDto orderById = orderFacade.findById(orderId);
        assertThat(orderById.order().status()).isEqualTo(OrderStatus.CANCELED.name());
    }

    @Test
    @DisplayName("Should throw OrderAccessException When warehouseman have no access to the order")
    public void should_throw_order_access_exception_when_warehouseman_have_no_access_to_the_order() {
        // given
        assertThatOrderDbIsEmpty();
        String warehouseName = "WH";
        mockWarehouseFacadeFindWarehouseByName(warehouseName, LARGE_BDF_SIZE, "manager@test.pl",
                "warehouseman1@test.pl");
        long bdfId = 1L;
        mockBdfFacadeGetBdfById(bdfId, LARGE_BDF_SIZE, 420, false, PLANNER_EMAIL);
        String completionDate = "15-06-2025";
        CreateOrderRequestDto request = createOrderRequestDto(Set.of(bdfId), completionDate, warehouseName);
        orderFacade.addOrder(request);
        assertThat(orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders()).hasSize(1);
        mockAuthenticatedUserProvider("warehouseman2@test.pl", "WAREHOUSEMAN");
        long orderId = 1L;
        // when
        Throwable throwable = catchThrowable(() -> orderFacade.cancelOrderById(orderId));
        // then
        assertThat(throwable).isInstanceOf(OrderAccessException.class);
        assertThat(throwable.getMessage()).isEqualTo("No access to order with id: " + orderId);
    }

    @Test
    @DisplayName("Should throw OrderAccessException When warehouseman have no access to the order")
    public void should_throw_order_access_exception_when_regional_manager_have_no_access_to_the_order() {
        // given
        assertThatOrderDbIsEmpty();
        String warehouseName = "WH";
        mockWarehouseFacadeFindWarehouseByName(warehouseName, LARGE_BDF_SIZE, "manager1@test.pl",
                "warehouseman1@test.pl");
        long bdfId = 1L;
        mockBdfFacadeGetBdfById(bdfId, LARGE_BDF_SIZE, 420, false, PLANNER_EMAIL);
        String completionDate = "15-06-2025";
        CreateOrderRequestDto request = createOrderRequestDto(Set.of(bdfId), completionDate, warehouseName);
        CreateOrderResponseDto createOrderResponseDto = orderFacade.addOrder(request);
        long orderId = createOrderResponseDto.order().id();
        assertThat(orderFacade.getOrders(DEFAULT_GET_REQUEST_DTO, Pageable.unpaged()).orders()).hasSize(1);
        mockAuthenticatedUserProvider("manager2@test.pl", "REGIONAL_MANAGER");
        // when
        Throwable throwable = catchThrowable(() -> orderFacade.cancelOrderById(orderId));
        // then
        assertThat(throwable).isInstanceOf(OrderAccessException.class);
        assertThat(throwable.getMessage()).isEqualTo("No access to order with id: " + orderId);
    }
}