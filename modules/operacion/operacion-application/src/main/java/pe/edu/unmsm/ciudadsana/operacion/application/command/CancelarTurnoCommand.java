package pe.edu.unmsm.ciudadsana.operacion.application.command;

import java.util.UUID;

public record CancelarTurnoCommand(UUID id, UUID tenantId) {}
