package com.github.lpgflow.domain.user.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetAllUsersWithDetailsResponseDto(List<UserWithDetailsDto> users) {
}
