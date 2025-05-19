package com.github.lpgflow.domain.util.messagesender;

import com.github.lpgflow.domain.util.enums.EmailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class EmailSenderImpl implements EmailSender {

    private final JavaMailSender mailService;

    @Value("$(spring.mail.username)")
    private String fromAddress;


    @Override
    public void sendMessage(EmailBody emailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailBody.to());
        message.setFrom(fromAddress);
        message.setSubject(emailBody.subject());
        message.setText(emailBody.text());
        mailService.send(message);
    }
}
