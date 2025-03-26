package com.github.lpgflow.domain.bdf.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record AssignCylinderToBdfRequestDto(

        @Min(1)
        int newQuantity) {
}
