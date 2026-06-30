package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ActualizarHorarioRequest(
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFin,
        String observacion
) {}
