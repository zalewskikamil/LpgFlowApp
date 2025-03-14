package com.github.lpgflow.domain.user.dto;

import lombok.Builder;

@Builder
public record RoleDto(Long id, String name) {
}
