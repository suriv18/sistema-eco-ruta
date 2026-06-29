package pe.edu.unmsm.ciudadsana.telemetria.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record EventoConectividadRegistradoEvent(
        UUID aggregateId,
        Instant ocurridoEn,
        UUID tenantId,
        String tipoEvento
) implements DomainEvent {

    @Override
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "EventoConectividad";
    }

    @Override
    public Instant getOcurridoEn() {
        return ocurridoEn;
    }
}
