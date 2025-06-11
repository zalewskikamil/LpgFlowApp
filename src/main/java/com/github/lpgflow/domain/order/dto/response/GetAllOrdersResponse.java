package com.github.lpgflow.domain.order.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetAllOrdersResponse(List<OrderDto> orders) {
}
