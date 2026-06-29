package pe.edu.unmsm.ciudadsana.ciudadano.application.command;

import java.util.UUID;

public record RegistrarCiudadanoCommand(
    UUID tenantId,
    String nombres,
    String apellidos,
    String email,
    String telefono,
    String documento
) {}
