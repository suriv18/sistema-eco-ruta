package pe.edu.unmsm.ciudadsana.auditoria.application.command;

import java.util.UUID;

public record RegistrarOutboxEventCommand(
        UUID tenantId,
        String aggregateType,
        UUID aggregateId,
        String eventType,
        String payload
) {}
