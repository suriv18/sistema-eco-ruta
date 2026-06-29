package pe.edu.unmsm.ciudadsana.telemetria.application.dto;

import java.time.Instant;
import java.util.UUID;

public record DesvioRutaResponseDto(
        UUID id,
        UUID tenantId,
        UUID unidadExternoId,
        UUID rutaExternoId,
        double latitud,
        double longitud,
        double distanciaDesvioM,
        String severidad,
        String estado,
        Instant detectadoEn,
        Instant atendidoEn
) {}
