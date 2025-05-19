package com.github.lpgflow.domain.user;

import com.github.lpgflow.domain.util.enums.EmailBody;
import com.github.lpgflow.domain.util.messagesender.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class PasswordResetOtpSender {

    private final UserRetriever userRetriever;
    private final OtpGenerator otpGenerator;
    private final OtpRepository otpRepository;
    private final EmailSender emailSender;

    @Value("${otp.expiration-minutes}")
    private int expirationMinutes;

    void generateAndSendOtp(String userEmail) {
        userRetriever.findByEmail(userEmail);
        otpRepository.deleteAllByUserEmail(userEmail);
        Otp otp = otpGenerator.generateOtp(userEmail);
        otpRepository.save(otp);
        EmailBody emailBody = EmailBody.builder()
                .to(userEmail)
                .subject("Forgot Password Request")
                .text(generateMessageText(otp.getOtp()))
                .build();
        try {
            emailSender.sendMessage(emailBody);
        } catch (MailException e) {
            throw new EmailSendingException("Failed to send email to " + userEmail);
        }
    }

    private String generateMessageText(String otp) {
        return String.format("""
                Hello,

                We received a request to reset your account password.

                Your One-Time Password (OTP) is:

                %s

                This code is valid for %d minute.

                If you did not request a password reset, you can safely ignore this email.

                Best regards,
                The LPGFlowApp Team
                """, otp, expirationMinutes);
    }
}
