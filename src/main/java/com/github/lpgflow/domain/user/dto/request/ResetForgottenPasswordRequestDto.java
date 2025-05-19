package com.github.lpgflow.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetForgottenPasswordRequestDto(

        @NotBlank(message = "Email must not be null and empty")
        @Email(message = "Incorrect email format")
        String userEmail,

        @NotBlank(message = "OTP must not be null and empty")
        String otp,

        @NotBlank(message = "New password must not be null and empty")
        String newPassword,

        @NotBlank(message = "Confirm new password must not be null and empty")
        String confirmNewPassword
) {
}
