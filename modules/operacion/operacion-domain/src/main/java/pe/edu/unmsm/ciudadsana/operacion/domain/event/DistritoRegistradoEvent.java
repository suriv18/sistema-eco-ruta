package pe.edu.unmsm.ciudadsana.operacion.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record DistritoRegistradoEvent(
    UUID aggregateId,
    Instant occurredOn,
    UUID tenantId,
    String nombre
) implements DomainEvent {
    @Override public UUID getAggregateId() { return aggregateId; }
    @Override public String getAggregateType() { return "Distrito"; }
    @Override public Instant getOcurridoEn() { return occurredOn; }
}
