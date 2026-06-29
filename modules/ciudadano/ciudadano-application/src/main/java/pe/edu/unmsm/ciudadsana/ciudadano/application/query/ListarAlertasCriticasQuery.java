package pe.edu.unmsm.ciudadsana.ciudadano.application.query;

import java.util.UUID;

public record ListarAlertasCriticasQuery(
    UUID tenantId,
    int page,
    int size
) {}
