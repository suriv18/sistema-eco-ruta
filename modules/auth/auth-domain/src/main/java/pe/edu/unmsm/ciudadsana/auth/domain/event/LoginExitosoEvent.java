package pe.edu.unmsm.ciudadsana.auth.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record LoginExitosoEvent(
        UUID aggregateId,
        Instant ocurridoEn,
        UUID tenantId,
        String username,
        String ipOrigen
) implements DomainEvent {

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "Usuario";
    }

    @Override
    public Instant getOcurridoEn() {
        return ocurridoEn;
    }
}
