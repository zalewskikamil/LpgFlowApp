package com.github.lpgflow.domain.bdf;

import com.github.lpgflow.domain.bdf.dto.request.CreateBdfRequestDto;
import com.github.lpgflow.domain.bdf.dto.response.BdfDto;
import com.github.lpgflow.domain.bdf.dto.response.CreateBdfResponseDto;
import com.github.lpgflow.domain.bdf.dto.response.GetBdfByIdDto;
import com.github.lpgflow.domain.bdf.dto.response.GetCurrentUserUnorderedBdfs;
import com.github.lpgflow.domain.util.enums.BdfSize;

import java.util.Set;
import java.util.stream.Collectors;

class BdfMapper {

    static GetBdfByIdDto mapFromBdfToGetBdfByIdDto(Bdf bdf) {
        return GetBdfByIdDto.builder()
                .bdf(mapFromBdfToBdfDto(bdf))
                .build();
    }

    static BdfDto mapFromBdfToBdfDto(Bdf bdf) {
        return BdfDto.builder()
                .id(bdf.getId())
                .size(bdf.getSize().name())
                .slots(bdf.getSize().getSlots())
                .cylinders(bdf.getCylinders().stream()
                        .map(BdfCylinderMapper::mapFromBdfCylinderToBdfCylinderDto)
                        .collect(Collectors.toSet()))
                .ordered(bdf.isOrdered())
                .createdBy(bdf.getCreatedBy())
                .build();
    }

    static GetCurrentUserUnorderedBdfs mapFromBdfsToGetCurrentUserUnorderedBdfsDto(Set<Bdf> bdfs) {
        return GetCurrentUserUnorderedBdfs.builder()
                .bdfs(bdfs.stream()
                        .map(BdfMapper::mapFromBdfToBdfDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    static CreateBdfResponseDto mapFromBdfToCreateBdfResponseDto(Bdf bdf) {
        return CreateBdfResponseDto.builder()
                .bdf(mapFromBdfToBdfDto(bdf))
                .build();
    }

    static Bdf mapFromCreateBdfRequestDtoToBdf(CreateBdfRequestDto request, String currentUserEmail) {
        BdfSize bdfSize;
        if (request.size() != null) {
            bdfSize = BdfSize.fromString(request.size());
        } else {
            bdfSize = BdfSize.LARGE;
        }
        boolean ordered = false;
        return new Bdf(bdfSize, ordered, currentUserEmail);
    }
}
