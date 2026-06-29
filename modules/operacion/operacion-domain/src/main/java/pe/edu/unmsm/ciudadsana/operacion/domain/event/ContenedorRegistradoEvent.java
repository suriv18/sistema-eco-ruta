package pe.edu.unmsm.ciudadsana.operacion.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record ContenedorRegistradoEvent(
    UUID aggregateId,
    Instant occurredOn,
    UUID tenantId,
    UUID zonaId
) implements DomainEvent {
    @Override public UUID getAggregateId() { return aggregateId; }
    @Override public String getAggregateType() { return "Contenedor"; }
    @Override public Instant getOcurridoEn() { return occurredOn; }
}
