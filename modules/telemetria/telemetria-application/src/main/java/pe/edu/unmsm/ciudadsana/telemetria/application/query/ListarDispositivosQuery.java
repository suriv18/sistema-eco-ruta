package pe.edu.unmsm.ciudadsana.telemetria.application.query;

import java.util.UUID;

public record ListarDispositivosQuery(
        UUID tenantId,
        int page,
        int size
) {}
