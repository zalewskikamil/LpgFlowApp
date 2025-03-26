package com.github.lpgflow.domain.bdf;

import com.github.lpgflow.domain.bdf.dto.response.BdfCylinderDto;

class BdfCylinderMapper {

    static BdfCylinderDto mapFromBdfCylinderToBdfCylinderDto(BdfCylinder bdfCylinder) {
        return BdfCylinderDto.builder()
                .cylinder(CylinderMapper.mapFromCylinderToCylinderDto(bdfCylinder.getCylinder()))
                .quantity(bdfCylinder.getQuantity())
                .build();
    }
}
