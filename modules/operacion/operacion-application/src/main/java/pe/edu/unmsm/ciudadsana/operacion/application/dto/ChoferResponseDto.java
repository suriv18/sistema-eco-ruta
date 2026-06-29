package pe.edu.unmsm.ciudadsana.operacion.application.dto;
import java.time.Instant;
import java.util.UUID;
public record ChoferResponseDto(UUID id, UUID tenantId, String nombres, String apellidos, String dni, String licencia, String telefono, String estado, Instant creadoEn) {}
