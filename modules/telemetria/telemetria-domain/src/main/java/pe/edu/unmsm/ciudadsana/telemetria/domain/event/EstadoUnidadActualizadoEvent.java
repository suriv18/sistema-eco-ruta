package pe.edu.unmsm.ciudadsana.telemetria.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record EstadoUnidadActualizadoEvent(
        UUID aggregateId,
        Instant ocurridoEn,
        UUID tenantId,
        String estadoMovimiento
) implements DomainEvent {

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "EstadoUnidad";
    }

    @Override
    public Instant getOcurridoEn() {
        return ocurridoEn;
    }
}
