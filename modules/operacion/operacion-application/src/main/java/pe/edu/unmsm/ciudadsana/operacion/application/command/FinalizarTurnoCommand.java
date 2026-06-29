package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.util.UUID;
public record FinalizarTurnoCommand(UUID turnoId, UUID tenantId) {}
