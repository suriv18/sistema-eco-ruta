package pe.edu.unmsm.ciudadsana.auth.application.command;

public record CrearRolCommand(
        String codigo,
        String nombre,
        String descripcion
) {}
