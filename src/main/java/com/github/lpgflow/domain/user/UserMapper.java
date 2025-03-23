package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.user.dto.UserDto;
import com.github.lpgflow.domain.user.dto.UserWithDetailsDto;
import com.github.lpgflow.domain.user.dto.request.CreateUserRequestDto;
import com.github.lpgflow.domain.user.dto.response.CreateUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.GetUserResponseDto;
import com.github.lpgflow.domain.user.dto.response.UserForSecurityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class UserMapper {

    private final PasswordEncoder passwordEncoder;

    CreateUserResponseDto mapFromUserToCreateUserRequestDto(User user) {
        return CreateUserResponseDto.builder()
                .user(mapFromUserToUserDto(user))
                .build();
    }

    private UserDto mapFromUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    GetUserResponseDto mapFromUserToGetUserResponseDto(User user) {
        return GetUserResponseDto.builder()
                .user(mapFromUserToUserDto(user))
                .build();
    }

    UserWithDetailsDto mapFromUserToUserWithDetailsDto(User user) {
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

    UserForSecurityDto mapFromUserToUserForSecurityDto(User user) {
        return UserForSecurityDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .enabled(user.isEnabled())
                .blocked(user.isBlocked())
                .build();
    }

    User mapFromCreateUserRequestDtoToUser(CreateUserRequestDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.password());
        return new User(
                dto.name(),
                dto.lastName(),
                dto.email(),
                encodedPassword,
                dto.phoneNumber());
    }
}
