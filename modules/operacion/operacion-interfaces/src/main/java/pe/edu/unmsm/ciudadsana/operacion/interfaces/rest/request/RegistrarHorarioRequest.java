package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record RegistrarHorarioRequest(
    @NotNull UUID zonaId,
    @Min(1) @Max(7) int diaSemana,
    @NotNull LocalTime horaInicio,
    @NotNull LocalTime horaFin,
    String observacion
) {}
