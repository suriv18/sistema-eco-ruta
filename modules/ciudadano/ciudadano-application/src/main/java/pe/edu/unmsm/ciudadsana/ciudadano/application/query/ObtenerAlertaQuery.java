package pe.edu.unmsm.ciudadsana.ciudadano.application.query;

import java.util.UUID;

public record ObtenerAlertaQuery(
    UUID alertaId,
    UUID tenantId
) {}
