package com.github.lpgflow.domain.order;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class DateConverter {

    static Instant toInstant(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.atStartOfDay(ZoneId.of("Europe/Warsaw")).toInstant();
    }

    static String fromInstant(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Europe/Warsaw"));
        return formatter.format(zonedDateTime);
    }
}
