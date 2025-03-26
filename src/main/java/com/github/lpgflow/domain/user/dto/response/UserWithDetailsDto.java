package com.github.lpgflow.domain.user.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserWithDetailsDto(
        Long id,
        String name,
        String lastName,
        String email,
        String phoneNumber,
        boolean enabled,
        boolean blocked,
        Set<RoleDto> roles
) {
}
