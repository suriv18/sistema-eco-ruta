package pe.edu.unmsm.ciudadsana.operacion.application.command;

import java.util.UUID;

public record ActivarDistritoCommand(UUID id, UUID tenantId) {}
