package pe.edu.unmsm.ciudadsana.telemetria.application.query;

import java.util.UUID;

public record ListarDesviosActivosQuery(
        UUID rutaExternoId,
        UUID tenantId,
        int page,
        int size
) {}
