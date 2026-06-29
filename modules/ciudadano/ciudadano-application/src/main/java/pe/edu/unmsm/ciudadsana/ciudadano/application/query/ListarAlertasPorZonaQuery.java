package pe.edu.unmsm.ciudadsana.ciudadano.application.query;

import java.util.UUID;

public record ListarAlertasPorZonaQuery(
    UUID zonaExternoId,
    UUID tenantId,
    int page,
    int size
) {}
