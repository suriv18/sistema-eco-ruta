package pe.edu.unmsm.ciudadsana.ciudadano.application.dto;

import java.time.Instant;
import java.util.UUID;

public record CiudadanoResponseDto(
    UUID id,
    UUID tenantId,
    String nombres,
    String apellidos,
    String email,
    String telefono,
    String documento,
    String estado,
    Instant creadoEn
) {}
