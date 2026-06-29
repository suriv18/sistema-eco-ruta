package pe.edu.unmsm.ciudadsana.rutas.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record RutaVersionAgregadaEvent(
        UUID aggregateId,
        Instant occurredOn,
        UUID tenantId,
        UUID rutaVersionId,
        int version,
        String motivo
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
