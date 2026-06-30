package pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record ActualizarPermisoRequest(
        @NotBlank String modulo,
        String descripcion
) {}
