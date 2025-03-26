package com.github.lpgflow.domain.bdf.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetAllCylindersResponseDto(List<CylinderDto> cylinders) {
}
