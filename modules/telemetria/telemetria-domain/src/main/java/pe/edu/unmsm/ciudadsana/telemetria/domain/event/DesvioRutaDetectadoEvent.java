package pe.edu.unmsm.ciudadsana.telemetria.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record DesvioRutaDetectadoEvent(
        UUID aggregateId,
        Instant ocurridoEn,
        UUID tenantId,
        UUID unidadExternoId,
        UUID rutaExternoId,
        double distanciaDesvioM
) implements DomainEvent {

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "DesvioRuta";
    }

    @Override
    public Instant getOcurridoEn() {
        return ocurridoEn;
    }
}
