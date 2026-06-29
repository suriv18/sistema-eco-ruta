package pe.edu.unmsm.ciudadsana.ciudadano.application.query;

import java.util.UUID;

public record ListarAlertasQuery(
    UUID tenantId,
    String estado,
    int page,
    int size
) {}
