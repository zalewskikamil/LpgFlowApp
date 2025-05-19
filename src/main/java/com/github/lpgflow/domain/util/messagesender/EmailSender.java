package com.github.lpgflow.domain.util.messagesender;

import com.github.lpgflow.domain.util.enums.EmailBody;

public interface EmailSender {

    void sendMessage(EmailBody emailBody);
}