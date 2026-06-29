package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.util.UUID;
public record RegistrarZonaCommand(UUID tenantId, UUID distritoId, String codigo, String nombre, String tipoZona, int prioridad) {}
