package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record ActualizarRolCommand(
        UUID rolId,
        String nombre,
        String descripcion
) {}
