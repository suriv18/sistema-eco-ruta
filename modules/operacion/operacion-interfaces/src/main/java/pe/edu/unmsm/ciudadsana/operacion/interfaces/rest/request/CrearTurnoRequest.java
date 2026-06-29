package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CrearTurnoRequest(
        @NotNull UUID unidadId,
        @NotNull UUID choferId,
        @NotNull UUID distritoId,
        @NotNull LocalDate fecha,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFin,
        @NotNull String tipoTurno
) {}
