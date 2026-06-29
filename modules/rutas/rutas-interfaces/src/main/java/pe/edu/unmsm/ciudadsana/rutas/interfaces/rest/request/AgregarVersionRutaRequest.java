package pe.edu.unmsm.ciudadsana.rutas.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record AgregarVersionRutaRequest(
        @NotBlank String motivo,
        UUID alertaIdExterno,
        @NotBlank String generadoPor,
        double distanciaM,
        int duracionS,
        double cargaKg,
        @NotEmpty List<NuevaParadaRequest> paradas
) {}
