package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RegistrarDistritoRequest(
        @NotBlank UUID tenantId,
        @NotBlank @Size(max = 120) String nombre,
        @Size(min = 6, max = 6) String ubigeo
) {}
