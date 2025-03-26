package com.github.lpgflow.domain.bdf.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateCylinderRequestDto(

        @NotBlank(message = "Capacity must not be null and empty")
        String capacity,

        @NotBlank(message = "Gas type must not be null and empty")
        String gasType,

        @NotBlank(message = "Usage type must not be null and empty")
        String usageType,

        boolean collar,

        String additionalInfo
) {
}
