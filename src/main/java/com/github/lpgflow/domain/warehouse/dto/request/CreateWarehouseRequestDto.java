package com.github.lpgflow.domain.warehouse.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateWarehouseRequestDto(

        @NotBlank(message = "Name must not be null and empty")
        String name,

        @NotBlank(message = "Regional manager email must not be null and empty")
        @Email(message = "Incorrect email format")
        String regionalManagerEmail,

        @NotBlank(message = "Warehouseman email must not be null and empty")
        @Email(message = "Incorrect email format")
        String warehousemanEmail,

        @NotBlank(message = "Bdf size must not be null and empty")
        String bdfSize,

        @NotNull(message = "Max cylinder without collar per bdf must not be null")
        int maxCylindersWithoutCollarPerBdf
) {
}
