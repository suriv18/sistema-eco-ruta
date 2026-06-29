package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.util.UUID;
public record IniciarTurnoCommand(UUID turnoId, UUID tenantId) {}
