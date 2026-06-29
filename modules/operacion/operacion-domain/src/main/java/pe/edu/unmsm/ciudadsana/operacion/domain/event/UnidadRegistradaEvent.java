package pe.edu.unmsm.ciudadsana.operacion.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record UnidadRegistradaEvent(
    UUID aggregateId,
    Instant occurredOn,
    UUID tenantId,
    String placa
) implements DomainEvent {
    @Override public UUID getAggregateId() { return aggregateId; }
    @Override public String getAggregateType() { return "Unidad"; }
    @Override public Instant getOcurridoEn() { return occurredOn; }
}
