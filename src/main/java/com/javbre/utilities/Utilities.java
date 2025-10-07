package com.javbre.utilities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Utilities {

    public static String getTimestampValue() {
        var zoneIdCo = ZoneId.of("America/Bogota");
        var now = ZonedDateTime.now(zoneIdCo);
        var dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        return now.truncatedTo(ChronoUnit.MILLIS).format(dtf);
    }

    // Convierte local datetime string + zone -> Instant
    public static Instant localToInstant(String localDateTimeIso, String zoneId) {
        LocalDateTime ldt = LocalDateTime.parse(localDateTimeIso);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of(zoneId));
        return zdt.toInstant();
    }

    // Convierte instant -> local datetime string usando zoneId
    public static String instantToLocalString(Instant instant, String zoneId) {
        ZonedDateTime zdt = instant.atZone(ZoneId.of(zoneId));
        return zdt.toLocalDateTime().toString();
    }
}
