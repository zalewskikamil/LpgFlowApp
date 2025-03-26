package com.github.lpgflow.domain.user.dto.response;

import lombok.Builder;

@Builder
public record RoleDto(Long id, String name) {
}
