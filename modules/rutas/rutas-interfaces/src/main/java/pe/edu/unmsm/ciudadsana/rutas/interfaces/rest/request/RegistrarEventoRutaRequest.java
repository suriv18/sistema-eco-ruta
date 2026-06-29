package pe.edu.unmsm.ciudadsana.rutas.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record RegistrarEventoRutaRequest(
        @NotBlank String tipoEvento,
        String descripcion,
        String datosJson
) {}
