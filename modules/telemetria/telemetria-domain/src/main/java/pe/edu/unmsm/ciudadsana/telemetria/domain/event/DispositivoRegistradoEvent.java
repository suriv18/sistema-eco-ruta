package pe.edu.unmsm.ciudadsana.telemetria.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record DispositivoRegistradoEvent(
        UUID aggregateId,
        Instant ocurridoEn,
        UUID tenantId,
        UUID unidadExternoId
) implements DomainEvent {

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "DispositivoGps";
    }

    @Override
    public Instant getOcurridoEn() {
        return ocurridoEn;
    }
}
