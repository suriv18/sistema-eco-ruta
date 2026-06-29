package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record RegistrarUsuarioCommand(
        UUID tenantId,
        String nombres,
        String apellidos,
        String email,
        String username,
        String password,
        String telefono
) {}
