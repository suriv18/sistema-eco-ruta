package pe.edu.unmsm.ciudadsana.auth.application.query;

import java.util.UUID;

public record ListarUsuariosQuery(
        UUID tenantId,
        int page,
        int size
) {}
