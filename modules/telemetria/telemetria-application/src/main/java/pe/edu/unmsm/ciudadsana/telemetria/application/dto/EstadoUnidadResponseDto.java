package pe.edu.unmsm.ciudadsana.telemetria.application.dto;

import java.time.Instant;
import java.util.UUID;

public record EstadoUnidadResponseDto(
        UUID unidadExternoId,
        UUID tenantId,
        UUID rutaExternoId,
        Double latitud,
        Double longitud,
        Double ultimaVelocidadKmh,
        Instant ultimoPingEn,
        String estadoMovimiento,
        Instant actualizadoEn
) {}
