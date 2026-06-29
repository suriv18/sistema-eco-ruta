package pe.edu.unmsm.ciudadsana.telemetria.application.query;

import java.util.UUID;

public record ListarPingsUnidadQuery(
        UUID unidadExternoId,
        UUID tenantId,
        int page,
        int size
) {}
