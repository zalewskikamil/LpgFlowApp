package com.github.lpgflow.domain.bdf;

import com.github.lpgflow.domain.bdf.dto.request.CreateCylinderRequestDto;
import com.github.lpgflow.domain.bdf.dto.response.CreateCylinderResponseDto;
import com.github.lpgflow.domain.bdf.dto.response.CylinderDto;
import com.github.lpgflow.domain.bdf.dto.response.GetAllCylindersResponseDto;
import com.github.lpgflow.domain.bdf.dto.response.GetCylinderDto;
import com.github.lpgflow.domain.util.CylinderCapacity;
import com.github.lpgflow.domain.util.CylinderUsageType;
import com.github.lpgflow.domain.util.GasType;

import java.util.List;
import java.util.stream.Collectors;

class CylinderMapper {

    static GetAllCylindersResponseDto mapFromListCylindersToGetAllCylindersResponseDto(List<Cylinder> cylinders) {
        return GetAllCylindersResponseDto.builder()
                .cylinders(cylinders.stream()
                        .map(CylinderMapper::mapFromCylinderToCylinderDto)
                        .collect(Collectors.toList()))
                .build();
    }

    static CylinderDto mapFromCylinderToCylinderDto(Cylinder cylinder) {
        return CylinderDto.builder()
                .id(cylinder.getId())
                .capacityInKg(cylinder.getCapacity().getCapacityInKg())
                .gasType(cylinder.getGasType().name())
                .usageType(cylinder.getUsageType().name())
                .hasCollar(cylinder.isCollar())
                .additionalInfo(cylinder.getAdditionalInfo())
                .bdfSlots(cylinder.getBdfSlots())
                .build();
    }

    static Cylinder mapFromCreateCylinderRequestDtoToCylinder(CreateCylinderRequestDto dto) {
        CylinderCapacity capacity = CylinderCapacity.fromString(dto.capacity());
        GasType gasType = GasType.fromString(dto.gasType());
        CylinderUsageType usageType = CylinderUsageType.fromString(dto.usageType());
        return new Cylinder(
                capacity,
                gasType,
                usageType,
                dto.collar(),
                dto.additionalInfo()
        );
    }

    static CreateCylinderResponseDto mapFromCylinderToCreateCylinderResponseDto(Cylinder cylinder) {
        return CreateCylinderResponseDto.builder()
                .cylinder(mapFromCylinderToCylinderDto(cylinder))
                .build();
    }

    static GetCylinderDto mapFromCylinderToGetCylinderDto(Cylinder cylinder) {
        return GetCylinderDto.builder()
                .cylinder(mapFromCylinderToCylinderDto(cylinder))
                .build();
    }
}
