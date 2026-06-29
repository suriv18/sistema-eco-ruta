package pe.edu.unmsm.ciudadsana.rutas.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CrearRutaRequest(
        @NotNull UUID turnoId,
        @NotNull UUID distritoId,
        @NotNull UUID depositoOrigenId,
        @NotNull UUID depositoDestinoId,
        @NotNull LocalDate fecha,
        @NotBlank String tipoRuta
) {}
