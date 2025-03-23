package com.github.lpgflow.infrastructure.security;

import lombok.Builder;

@Builder
public record JwtResponseDto(String token) {
}
