package pe.edu.unmsm.ciudadsana.operacion.application.command;

import java.util.UUID;

public record DesactivarDistritoCommand(UUID id, UUID tenantId) {}
