package pe.edu.unmsm.ciudadsana.ciudadano.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record AlertaRegistradaEvent(
        UUID aggregateId,
        Instant occurredOn,
        UUID tenantId,
        UUID distritoExternoId,
        String titulo
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
