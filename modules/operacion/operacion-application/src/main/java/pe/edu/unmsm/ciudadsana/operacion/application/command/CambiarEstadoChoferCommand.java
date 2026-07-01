package pe.edu.unmsm.ciudadsana.operacion.application.command;

import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoChofer;

import java.util.UUID;

public record CambiarEstadoChoferCommand(UUID id, UUID tenantId, EstadoChofer nuevoEstado) {}
