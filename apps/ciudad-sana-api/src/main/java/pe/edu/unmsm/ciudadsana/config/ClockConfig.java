package pe.edu.unmsm.ciudadsana.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.time.DateTimeProvider;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public DateTimeProvider dateTimeProvider(Clock clock) {
        return new DateTimeProvider() {
            @Override
            public Instant nowInstant() {
                return Instant.now(clock);
            }

            @Override
            public LocalDate nowDate() {
                return LocalDate.now(clock);
            }

            @Override
            public LocalDateTime nowDateTime() {
                return LocalDateTime.now(clock);
            }
        };
    }
}
