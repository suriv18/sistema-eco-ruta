package pe.edu.unmsm.ciudadsana.auditoria.application.dto;

import java.util.UUID;

public record RegistrarOutboxEventDto(
        UUID tenantId,
        String aggregateType,
        UUID aggregateId,
        String eventType,
        String payload
) {}
