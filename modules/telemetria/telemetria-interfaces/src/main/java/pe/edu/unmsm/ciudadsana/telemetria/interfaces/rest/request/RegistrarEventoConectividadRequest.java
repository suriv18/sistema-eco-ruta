package pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistrarEventoConectividadRequest(
        @NotNull UUID unidadExternoId,
        UUID dispositivoId,
        @NotBlank String tipoEvento,
        String detalle
) {}
