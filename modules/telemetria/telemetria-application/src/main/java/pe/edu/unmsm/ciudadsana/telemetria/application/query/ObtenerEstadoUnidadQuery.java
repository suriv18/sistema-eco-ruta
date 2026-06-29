package pe.edu.unmsm.ciudadsana.telemetria.application.query;

import java.util.UUID;

public record ObtenerEstadoUnidadQuery(
        UUID unidadExternoId,
        UUID tenantId
) {}
