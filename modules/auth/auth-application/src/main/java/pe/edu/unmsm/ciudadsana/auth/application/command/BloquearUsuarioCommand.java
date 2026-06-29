package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record BloquearUsuarioCommand(
        UUID usuarioId,
        UUID tenantId
) {}
