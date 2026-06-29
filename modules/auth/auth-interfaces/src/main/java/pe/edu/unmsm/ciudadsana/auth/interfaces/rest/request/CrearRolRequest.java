package pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record CrearRolRequest(
        @NotBlank String codigo,
        @NotBlank String nombre,
        String descripcion
) {}
