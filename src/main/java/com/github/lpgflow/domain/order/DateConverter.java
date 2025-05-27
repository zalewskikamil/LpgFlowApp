package com.github.lpgflow.domain.order;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class DateConverter {

    static Instant toInstant(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    static String fromInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);
        return formatter.format(zonedDateTime);
    }
}
