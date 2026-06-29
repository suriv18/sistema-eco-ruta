package pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record ActualizarRolRequest(
        @NotBlank String nombre,
        String descripcion
) {}
