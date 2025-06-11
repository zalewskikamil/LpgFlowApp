package com.github.lpgflow.domain.order.dto.response;

import lombok.Builder;

@Builder
public record GetOrderResponseDto(OrderDto order) {
}
