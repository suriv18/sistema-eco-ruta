package pe.edu.unmsm.ciudadsana.operacion.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record TurnoCreadoEvent(
    UUID aggregateId,
    Instant occurredOn,
    UUID tenantId,
    UUID unidadId,
    UUID choferId
) implements DomainEvent {
    @Override public UUID getAggregateId() { return aggregateId; }
    @Override public String getAggregateType() { return "Turno"; }
    @Override public Instant getOcurridoEn() { return occurredOn; }
}
