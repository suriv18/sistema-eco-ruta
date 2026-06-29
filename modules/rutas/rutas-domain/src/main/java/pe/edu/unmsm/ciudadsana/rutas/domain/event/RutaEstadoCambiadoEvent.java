package pe.edu.unmsm.ciudadsana.rutas.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record RutaEstadoCambiadoEvent(
        UUID aggregateId,
        Instant occurredOn,
        UUID tenantId,
        String estadoAnterior,
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
