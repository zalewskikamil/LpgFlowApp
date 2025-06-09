package feature;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@TestConfiguration
class FixedClockConfig {

    @Bean
    public Clock clock() {
        return Clock.fixed(Instant.parse("2025-06-04T00:00:00Z"), ZoneOffset.UTC);
    }
}
