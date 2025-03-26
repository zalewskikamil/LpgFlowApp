package com.github.lpgflow.domain.warehouse;

import com.github.lpgflow.domain.util.BdfSize;
import com.github.lpgflow.domain.warehouse.dto.request.CreateWarehouseRequestDto;
import com.github.lpgflow.domain.warehouse.dto.request.UpdateWarehousePartiallyRequestDto;
import com.github.lpgflow.domain.warehouse.dto.response.AssignAddressToWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.CreateWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetAllWarehousesResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehouseResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.GetWarehousesByRegionalManagerEmailResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.UpdateWarehousePartiallyResponseDto;
import com.github.lpgflow.domain.warehouse.dto.response.WarehouseDto;

import java.util.List;
import java.util.stream.Collectors;

class WarehouseMapper {


    static GetWarehouseResponseDto mapFromWarehouseToGetWarehouseResponseDto(Warehouse warehouse) {
        return GetWarehouseResponseDto.builder()
                .warehouse(mapFromWarehouseToWarehouseDto(warehouse))
                .build();
    }

    static WarehouseDto mapFromWarehouseToWarehouseDto(Warehouse warehouse) {
        return WarehouseDto.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .regionalManagerEmail(warehouse.getRegionalManagerEmail())
                .warehousemanEmail(warehouse.getWarehousemanEmail())
                .address(warehouse.getAddress() != null ?
                        AddressMapper.mapFromAddressToAddressDto(warehouse.getAddress()) : null)
                .bdfSize(warehouse.getBdfSize().name())
                .maxCylindersWithoutCollarPerBdf(warehouse.getMaxCylindersWithoutCollarPerBdf())
                .active(warehouse.getActive())
                .build();
    }

    static GetWarehousesByRegionalManagerEmailResponseDto mapFromListWarehousesToGetWarehousesByRegionalManagerEmailResponseDto
            (List<Warehouse> warehousesByRegionalManagerEmail) {
        return GetWarehousesByRegionalManagerEmailResponseDto.builder()
                .warehouses(warehousesByRegionalManagerEmail.stream()
                        .map(WarehouseMapper::mapFromWarehouseToWarehouseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    static CreateWarehouseResponseDto mapFromWarehouseToCreateWarehouseResponseDto(Warehouse warehouse) {
        return CreateWarehouseResponseDto.builder()
                .warehouse(mapFromWarehouseToWarehouseDto(warehouse))
                .build();
    }

    static UpdateWarehousePartiallyResponseDto mapFromWarehouseToUpdateWarehousePartiallyResponseDto(Warehouse warehouse) {
        return UpdateWarehousePartiallyResponseDto.builder()
                .warehouse(mapFromWarehouseToWarehouseDto(warehouse))
                .build();
    }

    static GetAllWarehousesResponseDto mapFromListWarehousesToGetAllWarehousesResponseDto(List<Warehouse> warehouses) {
        return GetAllWarehousesResponseDto.builder()
                .warehouses(warehouses.stream()
                        .map(WarehouseMapper::mapFromWarehouseToWarehouseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    static AssignAddressToWarehouseResponseDto mapFromWarehouseToAssignAddressToWarehouseResponseDto(
            Warehouse warehouse) {
        WarehouseDto warehouseDto = WarehouseMapper.mapFromWarehouseToWarehouseDto(warehouse);
        return new AssignAddressToWarehouseResponseDto(warehouseDto);
    }

    static Warehouse mapFromCreateWarehouseRequestDtoToWarehouse(CreateWarehouseRequestDto dto) {
        BdfSize bdfSize = BdfSize.fromString(dto.bdfSize());
        return new Warehouse(
                dto.name(),
                dto.regionalManagerEmail(),
                dto.warehousemanEmail(),
                false,
                bdfSize,
                dto.maxCylindersWithoutCollarPerBdf()
        );
    }

    static Warehouse mapFromUpdateWarehousePartiallyRequestDtoToWarehouse(UpdateWarehousePartiallyRequestDto dto) {
        return Warehouse.builder()
                .regionalManagerEmail(dto.regionalManagerEmail())
                .warehousemanEmail(dto.warehousemanEmail())
                .active(dto.active())
                .maxCylindersWithoutCollarPerBdf(dto.maxCylindersWithoutCollarPerBdf())
                .build();
    }
}
