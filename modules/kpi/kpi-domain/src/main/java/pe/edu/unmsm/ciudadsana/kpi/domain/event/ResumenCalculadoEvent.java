package pe.edu.unmsm.ciudadsana.kpi.domain.event;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ResumenCalculadoEvent(
        UUID resumenId,
        Instant ocurridoEn,
        UUID tenantId,
        UUID distritoId,
        LocalDate fecha
) implements DomainEvent {

    @Override
    public String getAggregateType() { return "ResumenOperativoDiario"; }

    @Override
    public UUID getAggregateId() { return resumenId; }

    @Override
    public Instant getOcurridoEn() { return ocurridoEn; }
}
