package com.github.lpgflow.domain.warehouse.dto.response;

import lombok.Builder;

@Builder
public record WarehouseDto(
        Long id,
        String name,
        String regionalManagerEmail,
        String warehousemanEmail,
        AddressDto address,
        String bdfSize,
        int maxCylindersWithoutCollarPerBdf,
        boolean active
) {
}
