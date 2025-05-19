package com.github.lpgflow.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Otp {

    @Id
    @GeneratedValue(generator = "otp_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "otp_id_seq",
            sequenceName = "otp_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private Instant issuedAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    Otp(String userEmail, String otp, Instant issuedAt, Instant expiresAt) {
        this.userEmail = userEmail;
        this.otp = otp;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }
}
