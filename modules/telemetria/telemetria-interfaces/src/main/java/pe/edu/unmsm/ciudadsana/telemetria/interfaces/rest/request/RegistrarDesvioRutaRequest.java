package pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistrarDesvioRutaRequest(
        @NotNull UUID unidadExternoId,
        @NotNull UUID rutaExternoId,
        @NotNull Double latitud,
        @NotNull Double longitud,
        @NotNull double distanciaDesvioM,
        @NotBlank String severidad
) {}
