package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record LogoutCommand(
        UUID usuarioId,
        String refreshToken
) {}
