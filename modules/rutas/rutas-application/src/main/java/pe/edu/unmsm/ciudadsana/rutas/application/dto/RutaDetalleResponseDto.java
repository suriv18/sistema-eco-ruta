package pe.edu.unmsm.ciudadsana.rutas.application.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record RutaDetalleResponseDto(
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
        List<RutaVersionDto> historialVersiones,
        List<RutaEventoDto> eventos,
        Instant creadoEn,
        Instant actualizadoEn
) {}
