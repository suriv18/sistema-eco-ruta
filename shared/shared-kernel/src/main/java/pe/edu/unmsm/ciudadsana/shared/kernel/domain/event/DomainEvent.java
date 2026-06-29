package pe.edu.unmsm.ciudadsana.shared.kernel.domain.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID getAggregateId();
    String getAggregateType();
    Instant getOcurridoEn();
}
