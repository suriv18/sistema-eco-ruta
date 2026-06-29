package pe.edu.unmsm.ciudadsana.telemetria.application.dto;

import java.time.Instant;
import java.util.UUID;

public record PingGpsResponseDto(
        UUID id,
        UUID tenantId,
        UUID dispositivoId,
        UUID unidadExternoId,
        UUID rutaExternoId,
        Instant ts,
        double latitud,
        double longitud,
        Double velocidadKmh,
        Double rumboGrados,
        Double precisionM,
        String origen,
        Instant recibidoEn
) {}
