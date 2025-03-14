package com.github.lpgflow.domain.user.dto.response;

import com.github.lpgflow.domain.user.dto.UserWithDetailsDto;
import lombok.Builder;

@Builder
public record GetUserWithDetailsResponseDto(UserWithDetailsDto user) {
}
