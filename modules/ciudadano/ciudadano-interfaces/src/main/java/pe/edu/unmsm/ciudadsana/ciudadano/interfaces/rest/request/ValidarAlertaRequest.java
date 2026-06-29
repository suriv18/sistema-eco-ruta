package pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ValidarAlertaRequest(
        boolean esDuplicada,
        UUID alertaOriginalId,
        boolean dentroGeocerca,
        @NotNull double scoreSpam,
        @NotBlank String resultado
) {}
