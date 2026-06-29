package pe.edu.unmsm.ciudadsana.telemetria.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record PingGpsRecibidoEvent(
        UUID aggregateId,
        Instant ocurridoEn,
        UUID tenantId,
        UUID unidadExternoId,
        double latitud,
        double longitud
) implements DomainEvent {

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "PingGps";
    }

    @Override
    public Instant getOcurridoEn() {
        return ocurridoEn;
    }
}
