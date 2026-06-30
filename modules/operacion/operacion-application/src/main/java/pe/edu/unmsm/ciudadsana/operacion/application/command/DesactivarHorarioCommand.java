package pe.edu.unmsm.ciudadsana.operacion.application.command;

import java.util.UUID;

public record DesactivarHorarioCommand(UUID tenantId, UUID horarioId) {}
