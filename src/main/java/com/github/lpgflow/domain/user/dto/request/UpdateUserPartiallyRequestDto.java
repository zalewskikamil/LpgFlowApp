package com.github.lpgflow.domain.user.dto.request;

import lombok.Builder;

@Builder
public record UpdateUserPartiallyRequestDto(
        Boolean enabled,
        Boolean blocked
) {
}
