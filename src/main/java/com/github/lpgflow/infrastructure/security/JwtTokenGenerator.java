package com.github.lpgflow.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
class JwtTokenGenerator {

    private final AuthenticationManager authenticationManager;
    private final Clock clock;
    private final JwtConfigurationProperties properties;
    private final KeyPair keyPair;

    String authenticateAndGenerateToken(String username, String password) {
        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticate);
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Instant issuedAt = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant expiresAt = issuedAt.plus(Duration.ofMinutes(properties.expirationMinutes()));
        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) keyPair.getPrivate());
        List<String> authoritiesAsString = securityUser.getAuthoritiesAsString();
        return JWT.create()
                .withSubject(securityUser.getUsername())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withIssuer(properties.issuer())
                .withClaim("roles", authoritiesAsString)
                .sign(algorithm);
    }
}
