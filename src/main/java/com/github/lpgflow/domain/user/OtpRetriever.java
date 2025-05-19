package com.github.lpgflow.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class OtpRetriever {

    private final OtpRepository otpRepository;

    Otp findByUserEmail(String userEmail) {
        return otpRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new OtpNotFoundException("OTP assigned to email " + userEmail + " not found"));
    }
}
