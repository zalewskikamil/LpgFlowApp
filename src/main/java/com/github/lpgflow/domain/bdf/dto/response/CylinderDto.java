package com.github.lpgflow.domain.bdf.dto.response;

import lombok.Builder;

@Builder
public record CylinderDto(
        long id,
        int capacityInKg,
        String gasType,
        String usageType,
        boolean hasCollar,
        String additionalInfo,
        int bdfSlots
) {
}
