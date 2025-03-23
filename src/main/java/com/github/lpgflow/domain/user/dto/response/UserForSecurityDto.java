package com.github.lpgflow.domain.user.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserForSecurityDto(
        String email,
        String password,
        Set<String> roles,
        boolean enabled,
        boolean blocked
) {
}
