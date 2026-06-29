package pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record CrearPermisoRequest(
        @NotBlank String codigo,
        @NotBlank String modulo,
        String descripcion
) {}
