package com.github.lpgflow.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdatePasswordRequestDto(

        @NotBlank(message = "Actual password must not be null and empty")
        String actualPassword,

        @NotBlank(message = "New password must not be null and empty")
        String newPassword,

        @NotBlank(message = "Confirm new password must not be null and empty")
        String confirmNewPassword
) {
}
