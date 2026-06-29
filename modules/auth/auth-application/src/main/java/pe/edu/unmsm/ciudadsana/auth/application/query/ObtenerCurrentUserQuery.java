package pe.edu.unmsm.ciudadsana.auth.application.query;

import java.util.UUID;

public record ObtenerCurrentUserQuery(
        UUID usuarioId,
        UUID tenantId
) {}
