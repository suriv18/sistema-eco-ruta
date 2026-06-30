package pe.edu.unmsm.ciudadsana.operacion.application.command;

import java.time.LocalTime;
import java.util.UUID;

public record ActualizarHorarioCommand(
        UUID tenantId,
        UUID horarioId,
        LocalTime horaInicio,
        LocalTime horaFin,
        String observacion
) {}
