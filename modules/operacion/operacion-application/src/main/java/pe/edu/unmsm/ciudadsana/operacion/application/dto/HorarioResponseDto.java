package pe.edu.unmsm.ciudadsana.operacion.application.dto;

import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public record HorarioResponseDto(
    UUID id,
    UUID tenantId,
    UUID zonaId,
    int diaSemana,
    LocalTime horaInicio,
    LocalTime horaFin,
    String observacion,
    String estado,
    Instant creadoEn
) {
    public static HorarioResponseDto from(HorarioRecoleccion h) {
        return new HorarioResponseDto(
            h.getId().value(),
            h.getTenantId().value(),
            h.getZonaId().value(),
            h.getDiaSemana(),
            h.getHoraInicio(),
            h.getHoraFin(),
            h.getObservacion(),
            h.getEstado().name(),
            h.getCreadoEn()
        );
    }
}
