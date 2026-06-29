package pe.edu.unmsm.ciudadsana.rutas.application.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RutaVersionDto(
        UUID id,
        UUID rutaId,
        int version,
        String motivo,
        UUID alertaIdExterno,
        String generadoPor,
        MetricasRutaDto metricas,
        List<RutaParadaDto> paradas,
        Instant creadoEn
) {}
