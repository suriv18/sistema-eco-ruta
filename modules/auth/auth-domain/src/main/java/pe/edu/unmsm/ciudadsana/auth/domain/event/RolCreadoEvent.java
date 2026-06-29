package pe.edu.unmsm.ciudadsana.auth.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record RolCreadoEvent(
        UUID aggregateId,
        Instant ocurridoEn,
        String codigo,
        String nombre
) implements DomainEvent {

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "Rol";
    }

    @Override
    public Instant getOcurridoEn() {
        return ocurridoEn;
    }
}
