package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.util.UUID;
public record CambiarEstadoUnidadCommand(UUID unidadId, UUID tenantId, String nuevoEstado) {}
