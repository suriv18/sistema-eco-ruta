package pe.edu.unmsm.ciudadsana.auth.application.command;

import java.util.UUID;

public record AsignarPermisoARolCommand(UUID rolId, UUID permisoId) {}
