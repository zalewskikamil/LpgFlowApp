package com.github.lpgflow.domain.user.dto.response;

import com.github.lpgflow.domain.user.dto.RoleDto;
import lombok.Builder;

@Builder
public record GetRoleResponseDto(RoleDto role) {
}
