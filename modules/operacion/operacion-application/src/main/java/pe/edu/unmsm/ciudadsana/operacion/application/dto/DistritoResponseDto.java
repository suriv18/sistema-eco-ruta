package pe.edu.unmsm.ciudadsana.operacion.application.dto;
import java.time.Instant;
import java.util.UUID;
public record DistritoResponseDto(UUID id, UUID tenantId, String nombre, String ubigeo, String estado, Instant creadoEn) {}
