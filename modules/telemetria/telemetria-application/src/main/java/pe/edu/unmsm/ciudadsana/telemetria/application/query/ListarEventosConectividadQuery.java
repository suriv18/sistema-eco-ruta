package pe.edu.unmsm.ciudadsana.telemetria.application.query;

import java.util.UUID;

public record ListarEventosConectividadQuery(
        UUID unidadExternoId,
        UUID tenantId,
        int page,
        int size
) {}
