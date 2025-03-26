package com.github.lpgflow.domain.warehouse.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetWarehousesByRegionalManagerEmailResponseDto(List<WarehouseDto> warehouses) {

}
