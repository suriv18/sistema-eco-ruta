package pe.edu.unmsm.ciudadsana.auditoria.application.dto;

import java.time.Instant;
import java.util.UUID;

public record OutboxEventDto(
        UUID outboxId,
        UUID tenantId,
        String aggregateType,
        UUID aggregateId,
        String eventType,
        String payload,
        String estado,
        Instant creadoEn,
        Instant publicadoEn,
        String errorMensaje
) {}
