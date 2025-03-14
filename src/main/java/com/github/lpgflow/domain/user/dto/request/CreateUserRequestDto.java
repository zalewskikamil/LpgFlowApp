package com.github.lpgflow.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateUserRequestDto(

        @NotBlank(message = "Name must not be null and empty")
        String name,

        @NotBlank(message = "Last name must not be null and empty")
        String lastName,

        @NotBlank(message = "Email must not be null and empty")
        @Email(message = "Incorrect email format")
        String email,

        @NotBlank(message = "Password must not be null and empty")
        String password,

        @NotBlank(message = "Phone number must not be null and empty")
        @Pattern(regexp = "^[1-9]\\d{8}$", message = "Phone number must be 9 digits long and cannot start with 0")
        String phoneNumber) {
}
