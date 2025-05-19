package com.github.lpgflow.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

@Component
@RequiredArgsConstructor
class OtpGenerator {

    private final Random random = new Random();

    private final Clock clock;

    @Value("${otp.expiration-minutes}")
    private int expirationMinutes;

    Otp generateOtp(String userEmail) {
        int otpAsNumber = random.nextInt(100_000, 1_000_000);
        Instant issuedAt = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        return Otp.builder()
                .userEmail(userEmail)
                .otp(String.valueOf(otpAsNumber))
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plus(Duration.ofMinutes(expirationMinutes)))
                .build();
    }
}
