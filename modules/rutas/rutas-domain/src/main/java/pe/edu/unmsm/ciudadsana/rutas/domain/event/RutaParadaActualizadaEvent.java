package pe.edu.unmsm.ciudadsana.rutas.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record RutaParadaActualizadaEvent(
        UUID aggregateId,
        Instant occurredOn,
        UUID tenantId,
        UUID paradaId,
        String estadoNuevo
) implements DomainEvent {

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "Ruta";
    }

    @Override
    public Instant getOcurridoEn() {
        return occurredOn;
    }
}
