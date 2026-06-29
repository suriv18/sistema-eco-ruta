package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record ActivarUsuarioCommand(
        UUID usuarioId,
        UUID tenantId
) {}
