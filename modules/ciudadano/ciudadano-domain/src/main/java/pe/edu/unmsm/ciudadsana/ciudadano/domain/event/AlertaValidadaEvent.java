package pe.edu.unmsm.ciudadsana.ciudadano.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record AlertaValidadaEvent(
        UUID aggregateId,
        Instant occurredOn,
        UUID tenantId,
        String resultado
) implements DomainEvent {

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "AlertaCiudadana";
    }

    @Override
    public Instant getOcurridoEn() {
        return occurredOn;
    }
}
