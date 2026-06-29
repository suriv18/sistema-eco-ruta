package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record QuitarPermisoDeRolCommand(UUID rolId, UUID permisoId) {}
