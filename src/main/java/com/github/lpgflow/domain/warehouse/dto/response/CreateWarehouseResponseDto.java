package com.github.lpgflow.domain.warehouse.dto.response;

import lombok.Builder;

@Builder
public record CreateWarehouseResponseDto(WarehouseDto warehouse) {
}