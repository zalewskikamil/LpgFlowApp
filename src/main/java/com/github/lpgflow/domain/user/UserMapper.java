package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.user.dto.request.CreateUserRequestDto;
import com.github.lpgflow.domain.user.dto.request.UpdateUserPartiallyRequestDto;
import com.github.lpgflow.domain.user.dto.response.CreateUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetAllUsersWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserWithDetailsResponseDto;
import com.github.lpgflow.domain.user.dto.response.UpdateUserPartiallyResponseDto;
import com.github.lpgflow.domain.user.dto.response.UserDto;
import com.github.lpgflow.domain.user.dto.response.UserForSecurityDto;
import com.github.lpgflow.domain.user.dto.response.UserWithDetailsDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class UserMapper {

    static CreateUserResponseDto mapFromUserToCreateUserResponseDto(User user) {
        return CreateUserResponseDto.builder()
                .user(mapFromUserToUserDto(user))
                .build();
    }

    static private UserDto mapFromUserToUserDto(User user) {
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
                .enabled(user.getEnabled())
                .blocked(user.getBlocked())
                .roles(user.getRoles().stream()
                        .map(RoleMapper::mapFromRoleToRoleDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    static UserForSecurityDto mapFromUserToUserForSecurityDto(User user) {
        return UserForSecurityDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .enabled(user.getEnabled())
                .blocked(user.getBlocked())
                .build();
    }

    static User mapFromCreateUserRequestDtoToUser(CreateUserRequestDto dto) {
        return new User(
                dto.name(),
                dto.lastName(),
                dto.email(),
                dto.password(),
                dto.phoneNumber());
    }

    static GetAllUsersWithDetailsResponseDto mapFromListUsersToGetAllUsersWithDetailsResponseDto(List<User> users) {
        return GetAllUsersWithDetailsResponseDto.builder()
                .users(users.stream()
                        .map(UserMapper::mapFromUserToUserWithDetailsDto)
                        .collect(Collectors.toList()))
                .build();
    }

    static GetUserWithDetailsResponseDto mapFromUserToGetUserWithDetailsResponseDto(User user) {
        return GetUserWithDetailsResponseDto.builder()
                .user(mapFromUserToUserWithDetailsDto(user))
                .build();
    }

    static User mapFromUpdateUserPartiallyRequestDtoToUser(UpdateUserPartiallyRequestDto request) {
        return User.builder()
                .enabled(request.enabled())
                .blocked(request.blocked())
                .build();
    }

    static UpdateUserPartiallyResponseDto mapFromUserToUpdateUserPartiallyResponseDto(User user) {
        return UpdateUserPartiallyResponseDto.builder()
                .user(mapFromUserToUserWithDetailsDto(user))
                .build();
    }
}
