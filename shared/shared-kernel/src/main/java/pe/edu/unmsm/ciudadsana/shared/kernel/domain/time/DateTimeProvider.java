package pe.edu.unmsm.ciudadsana.shared.kernel.domain.time;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeProvider {
    Instant nowInstant();
    LocalDate nowDate();
    LocalDateTime nowDateTime();
}
