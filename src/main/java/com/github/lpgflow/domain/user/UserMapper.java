package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.user.dto.UserDto;
import com.github.lpgflow.domain.user.dto.UserWithDetailsDto;
import com.github.lpgflow.domain.user.dto.request.CreateUserRequestDto;
import com.github.lpgflow.domain.user.dto.response.CreateUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserResponseDto;

import java.util.stream.Collectors;

class UserMapper {

    static CreateUserResponseDto mapFromUserToCreateUserRequestDto(User user) {
        return CreateUserResponseDto.builder()
                .user(mapFromUserToUserDto(user))
                .build();
    }

    private static UserDto mapFromUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    static GetUserResponseDto mapFromUserToGetUserResponseDto(User user) {
        return GetUserResponseDto.builder()
                .user(mapFromUserToUserDto(user))
                .build();
    }

    static UserWithDetailsDto mapFromUserToUserWithDetailsDto(User user) {
        return UserWithDetailsDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .enabled(user.isEnabled())
                .blocked(user.isBlocked())
                .roles(user.getRoles().stream()
                        .map(RoleMapper::mapFromRoleToRoleDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    static User mapFromCreateUserRequestDtoToUser(CreateUserRequestDto dto) {
        return new User(dto.name(), dto.lastName(), dto.email(), dto.password(), dto.phoneNumber());
    }
}
