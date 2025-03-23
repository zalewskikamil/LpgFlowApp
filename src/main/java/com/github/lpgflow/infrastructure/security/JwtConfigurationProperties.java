package com.github.lpgflow.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "auth.jwt")
public record JwtConfigurationProperties(
        long expirationMinutes,
        String issuer
) {
}