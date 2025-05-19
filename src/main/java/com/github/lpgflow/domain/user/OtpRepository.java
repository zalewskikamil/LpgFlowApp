package com.github.lpgflow.domain.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

interface OtpRepository extends Repository<Otp, Long> {

    Optional<Otp> findByUserEmail(String userEmail);

    Otp save(Otp otp);

    @Modifying
    @Query("DELETE FROM Otp o WHERE o.userEmail = :userEmail")
    void deleteAllByUserEmail(String userEmail);
}
