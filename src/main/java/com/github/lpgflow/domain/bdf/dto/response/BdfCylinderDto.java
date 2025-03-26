package com.github.lpgflow.domain.bdf.dto.response;

import lombok.Builder;

@Builder
public record BdfCylinderDto(
        CylinderDto cylinder,
        int quantity) {
}
