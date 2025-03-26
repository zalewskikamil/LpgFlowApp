package com.github.lpgflow.domain.bdf.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record BdfDto(
        Long id,
        String size,
        int slots,
        Set<BdfCylinderDto> cylinders,
        boolean ordered,
        String createdBy
) {
}
