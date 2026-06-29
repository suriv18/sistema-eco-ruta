package pe.edu.unmsm.ciudadsana.telemetria.application.dto;

import java.time.Instant;
import java.util.UUID;

public record DispositivoResponseDto(
        UUID id,
        UUID tenantId,
        UUID unidadExternoId,
        String imei,
        String proveedor,
        String estado,
        Instant ultimoPingEn,
        Instant creadoEn
) {}
