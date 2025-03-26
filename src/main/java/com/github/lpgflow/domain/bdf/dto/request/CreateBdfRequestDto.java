package com.github.lpgflow.domain.bdf.dto.request;

import lombok.Builder;

@Builder
public record CreateBdfRequestDto(String size) {
}
