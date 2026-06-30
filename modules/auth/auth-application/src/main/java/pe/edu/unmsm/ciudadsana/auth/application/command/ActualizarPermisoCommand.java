package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record ActualizarPermisoCommand(UUID permisoId, String modulo, String descripcion) {}
