package pe.edu.unmsm.ciudadsana.rutas.application.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record RutaResponseDto(
        UUID id,
        UUID tenantId,
        UUID turnoId,
        UUID distritoId,
        UUID depositoOrigenId,
        UUID depositoDestinoId,
        LocalDate fecha,
        String tipoRuta,
        String estado,
        MetricasRutaDto metricas,
        RutaVersionDto versionActual,
        Instant creadoEn,
        Instant actualizadoEn
) {}
