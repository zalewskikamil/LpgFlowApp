package com.github.lpgflow.infrastructure.order;

import com.github.lpgflow.domain.order.OrderFacade;
import com.github.lpgflow.domain.order.dto.request.CreateOrderRequestDto;
import com.github.lpgflow.domain.order.dto.request.GetOrdersRequestDto;
import com.github.lpgflow.domain.order.dto.response.CreateOrderResponseDto;
import com.github.lpgflow.domain.order.dto.response.GetAllOrdersResponse;
import com.github.lpgflow.domain.order.dto.response.GetOrderResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping("/search")
    public ResponseEntity<GetAllOrdersResponse> getAllOrders(@RequestBody @Valid GetOrdersRequestDto request,
                                                             @PageableDefault(page = 0, size = 10) Pageable pageable) {
        GetAllOrdersResponse response = orderFacade.getOrders(request, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetOrderResponseDto> getOrderById(@PathVariable Long id) {
        GetOrderResponseDto response = orderFacade.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody @Valid CreateOrderRequestDto request) {
        CreateOrderResponseDto response = orderFacade.addOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> cancelOrderById(@PathVariable Long id) {
        orderFacade.cancelOrderById(id);
        return ResponseEntity.ok().build();
    }
}
