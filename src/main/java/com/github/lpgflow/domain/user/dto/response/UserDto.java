package com.github.lpgflow.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserDto(Long id, String name, String lastName, String email, String phoneNumber) {
}
