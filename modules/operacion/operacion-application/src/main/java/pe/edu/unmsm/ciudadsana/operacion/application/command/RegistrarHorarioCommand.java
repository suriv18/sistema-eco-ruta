package pe.edu.unmsm.ciudadsana.operacion.application.command;

import java.time.LocalTime;
import java.util.UUID;

public record RegistrarHorarioCommand(
    UUID tenantId,
    UUID zonaId,
    int diaSemana,
    LocalTime horaInicio,
    LocalTime horaFin,
    String observacion
) {}
