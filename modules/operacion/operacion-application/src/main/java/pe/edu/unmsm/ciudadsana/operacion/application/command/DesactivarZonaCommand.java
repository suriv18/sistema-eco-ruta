package pe.edu.unmsm.ciudadsana.operacion.application.command;
import java.util.UUID;
public record DesactivarZonaCommand(UUID zonaId, UUID tenantId) {}
