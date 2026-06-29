package pe.edu.unmsm.ciudadsana.operacion.application.dto;
import java.time.Instant;
import java.util.UUID;
public record ZonaResponseDto(UUID id, UUID tenantId, UUID distritoId, String codigo, String nombre, String tipoZona, int prioridad, String estado, Instant creadoEn) {}
