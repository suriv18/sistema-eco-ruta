package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.util.UUID;
public record CambiarEstadoContenedorCommand(UUID contenedorId, UUID tenantId, String nuevoEstado) {}
