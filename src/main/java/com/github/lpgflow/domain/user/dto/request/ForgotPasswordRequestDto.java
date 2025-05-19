package com.github.lpgflow.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequestDto(

        @NotBlank(message = "Email must not be null and empty")
        @Email(message = "Incorrect email format")
        String userEmail
) {
}
