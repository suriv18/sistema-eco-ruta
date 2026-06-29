package pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ProcesarPingGpsRequest(
        @NotNull UUID dispositivoId,
        @NotNull UUID unidadExternoId,
        UUID rutaExternoId,
        @NotNull Instant ts,
        @NotNull Double latitud,
        @NotNull Double longitud,
        Double velocidadKmh,
        Double rumboGrados,
        Double precisionM,
        @NotBlank String origen
) {}
