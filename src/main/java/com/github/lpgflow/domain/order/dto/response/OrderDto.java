package com.github.lpgflow.domain.order.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderDto(Long id,
                       List<Long> bdfIds,
                       String createdBy,
                       String completionDate,
                       String warehouseName,
                       String status) {
}
