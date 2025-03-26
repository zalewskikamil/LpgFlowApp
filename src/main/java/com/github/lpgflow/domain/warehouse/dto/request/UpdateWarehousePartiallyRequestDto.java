package com.github.lpgflow.domain.warehouse.dto.request;

import lombok.Builder;

@Builder
public record UpdateWarehousePartiallyRequestDto(
        String regionalManagerEmail,
        String warehousemanEmail,
        Boolean active,
        Integer maxCylindersWithoutCollarPerBdf) {
}
