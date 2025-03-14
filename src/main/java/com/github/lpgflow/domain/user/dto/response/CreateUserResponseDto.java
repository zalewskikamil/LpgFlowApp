package com.github.lpgflow.domain.user.dto.response;

import com.github.lpgflow.domain.user.dto.UserDto;
import lombok.Builder;

@Builder
public record CreateUserResponseDto(UserDto user) {
}
