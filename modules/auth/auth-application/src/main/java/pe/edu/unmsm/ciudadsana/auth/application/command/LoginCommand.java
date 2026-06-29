package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record LoginCommand(
        String username,
        String password,
        UUID tenantId,
        String ipOrigen,
        String userAgent
) {}
