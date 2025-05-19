package com.github.lpgflow.domain.util.enums;

import lombok.Builder;

@Builder
public record EmailBody(String to, String subject, String text) {
}
