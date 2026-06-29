package pe.edu.unmsm.ciudadsana.auditoria.application.query;

import java.util.UUID;

public record ListarOutboxEventsQuery(
        UUID tenantId,
        String estado,
        String eventType,
        int page,
        int size
) {}
