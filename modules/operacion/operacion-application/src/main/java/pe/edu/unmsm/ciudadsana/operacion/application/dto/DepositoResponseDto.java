package pe.edu.unmsm.ciudadsana.operacion.application.dto;
import java.time.Instant;
import java.util.UUID;
public record DepositoResponseDto(UUID id, UUID tenantId, UUID distritoId, String nombre, String tipo, String estado, Instant creadoEn) {}
