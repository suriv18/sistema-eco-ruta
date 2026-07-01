package pe.edu.unmsm.ciudadsana.operacion.application.command;

import java.util.UUID;

public record ActualizarZonaCommand(UUID tenantId, UUID zonaId, Integer prioridad) {}
