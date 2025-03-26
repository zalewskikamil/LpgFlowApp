package com.github.lpgflow.domain.bdf.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record UpdateCylindersAssignedToBdfRequestDto(

        @Min(2)
        long cylinderId,

        @Min(0)
        int newQuantity
) {
}
