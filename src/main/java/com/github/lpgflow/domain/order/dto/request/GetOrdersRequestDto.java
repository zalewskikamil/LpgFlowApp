package com.github.lpgflow.domain.order.dto.request;

import jakarta.validation.constraints.Pattern;

import java.util.List;

public record GetOrdersRequestDto(

        String orderStatus,

        @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Date must be in dd-MM-yyyy format")
        String from,

        @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Date must be in dd-MM-yyyy format")
        String to,

        List<String> warehouseNames
) {
}
