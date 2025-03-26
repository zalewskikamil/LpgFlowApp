package com.github.lpgflow.domain.warehouse;

import com.github.lpgflow.domain.warehouse.dto.request.CreateAddressRequestDto;
import com.github.lpgflow.domain.warehouse.dto.response.AddressDto;
import com.github.lpgflow.domain.warehouse.dto.response.CreateAddressResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAddressResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAllAddressesResponseDto;

import java.util.List;
import java.util.stream.Collectors;

class AddressMapper {

    public static GetAddressResponseDto mapFromAddressToGetAddressResponseDto(Address address) {
        return GetAddressResponseDto.builder()
                .address(mapFromAddressToAddressDto(address))
                .build();
    }

    public static AddressDto mapFromAddressToAddressDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .build();
    }

    public static Address mapFromCreateAddressRequestDtoToAddress(CreateAddressRequestDto dto) {
        return new Address(
                dto.street(),
                dto.city(),
                dto.postalCode()
        );
    }

    public static CreateAddressResponseDto mapFromAddressToCreateAddressResponseDto(Address address) {
        return CreateAddressResponseDto.builder()
                .address(mapFromAddressToAddressDto(address))
                .build();
    }

    public static GetAllAddressesResponseDto mapFromListAddressesToGetAllAddressesResponseDto(List<Address> addresses) {
        return GetAllAddressesResponseDto.builder()
                .addresses(addresses.stream()
                        .map(AddressMapper::mapFromAddressToAddressDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
