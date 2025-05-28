package com.github.lpgflow.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.Set;

@Builder
public record CreateOrderRequestDto(

        @NotEmpty(message = "You must provide at least one ID")
        Set<@NotNull(message = "ID must not be null") Long> bdfIds,

        @NotBlank(message = "Scheduled completion date must not be null and empty")
        @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Date must be in dd-MM-yyyy format")
        String scheduledCompletionDate,

        @NotBlank(message = "Warehouse name must not be null and empty")
        String warehouseName
) {
}
