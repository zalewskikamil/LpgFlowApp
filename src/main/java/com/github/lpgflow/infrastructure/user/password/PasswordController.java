package com.github.lpgflow.infrastructure.user.password;

import com.github.lpgflow.domain.user.UserFacade;
import com.github.lpgflow.domain.user.dto.request.ForgotPasswordRequestDto;
import com.github.lpgflow.domain.user.dto.request.ResetForgottenPasswordRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forgot_password")
class PasswordController {

    private final UserFacade userFacade;

    @PostMapping("/sent")
    public ResponseEntity<String> forgotPassword(@Valid ForgotPasswordRequestDto request) {
        String userEmail = request.userEmail();
        userFacade.forgotPassword(userEmail);
        return ResponseEntity.ok().body("Your one-time password has been sent. Please check your mailbox");
    }

    @PatchMapping("/reset")
    public ResponseEntity<Void> resetForgottenPassword(@Valid ResetForgottenPasswordRequestDto request) {
        userFacade.resetForgottenPassword(request);
        return ResponseEntity.ok().build();
    }
}
