package pe.edu.unmsm.ciudadsana.operacion.application.dto;

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
) {}
