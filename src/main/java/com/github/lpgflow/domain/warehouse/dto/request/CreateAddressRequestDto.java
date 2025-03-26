package com.github.lpgflow.domain.warehouse.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateAddressRequestDto(

        @NotBlank(message = "Street must not be null and empty")
        String street,

        @NotBlank(message = "City must not be null and empty")
        String city,

        @NotBlank(message = "Postal code must not be null and empty")
        @Pattern(regexp = "\\d{2}-\\d{3}", message = "Postal code must be in the format XX-XXX")
        String postalCode
) {
}
