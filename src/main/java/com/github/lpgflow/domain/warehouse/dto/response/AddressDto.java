package com.github.lpgflow.domain.warehouse.dto.response;

import lombok.Builder;

@Builder
public record AddressDto(Long id, String street, String city, String postalCode) {
}
