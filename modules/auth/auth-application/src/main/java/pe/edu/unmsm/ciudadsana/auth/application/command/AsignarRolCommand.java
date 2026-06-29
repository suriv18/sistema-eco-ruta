package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record AsignarRolCommand(
        UUID usuarioId,
        UUID rolId,
        UUID tenantId
) {}
