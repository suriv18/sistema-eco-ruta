package pe.edu.unmsm.ciudadsana.telemetria.application.dto;

import java.time.Instant;
import java.util.UUID;

public record EventoConectividadResponseDto(
        UUID id,
        UUID tenantId,
        UUID unidadExternoId,
        UUID dispositivoId,
        String tipoEvento,
        String detalle,
        Instant detectadoEn
) {}
