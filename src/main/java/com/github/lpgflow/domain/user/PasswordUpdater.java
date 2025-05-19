package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.user.dto.request.ResetForgottenPasswordRequestDto;
import com.github.lpgflow.domain.user.dto.request.UpdatePasswordRequestDto;
import com.github.lpgflow.infrastructure.security.AuthenticatedUserProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
class PasswordUpdater {

    private final UserRetriever userRetriever;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final OtpRetriever otpRetriever;
    private final OtpRepository otpRepository;

    void changePassword(UpdatePasswordRequestDto request) {
        String newPassword = request.newPassword();
        if (!newPassword.equals(request.confirmNewPassword())) {
            throw new ChangePasswordException("New password and confirm new password are not the same");
        }
        String actualPassword = request.actualPassword();
        if (actualPassword.equals(newPassword)) {
            throw new ChangePasswordException("The new password must be different from the current one");
        }
        String currentUserEmail = authenticatedUserProvider.getCurrentUserName();
        User user = userRetriever.findByEmail(currentUserEmail);
        if (!passwordEncoder.matches(actualPassword, user.getPassword())) {
            throw new ChangePasswordException("Wrong actual password");
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        log.info("Updating user password with id: {}", user.getId());
        userRepository.save(user);
    }

    void resetForgottenPassword(ResetForgottenPasswordRequestDto request) {
        String newPassword = request.newPassword();
        if (!newPassword.equals(request.confirmNewPassword())) {
            throw new ResetPasswordException("New password and confirm new password are not the same");
        }
        String userEmail = request.userEmail();
        Otp otpByUserEmail = otpRetriever.findByUserEmail(userEmail);
        if (otpByUserEmail.isUsed()) {
            throw new ResetPasswordException("OTP is already used");
        }
        String otpAsStringFromDatabase = otpByUserEmail.getOtp();
        if (!otpAsStringFromDatabase.equals(request.otp()) ) {
            throw new ResetPasswordException("Wrong OTP");
        }
        User user = userRetriever.findByEmail(userEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
        log.info("Resetting user password with id: {}", user.getId());
        userRepository.save(user);
        otpByUserEmail.setUsed(true);
        otpRepository.save(otpByUserEmail);
    }
}
