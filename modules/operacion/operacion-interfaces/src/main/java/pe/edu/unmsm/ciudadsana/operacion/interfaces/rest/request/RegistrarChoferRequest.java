package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrarChoferRequest(
        @NotBlank @Size(max = 120) String nombres,
        @NotBlank @Size(max = 120) String apellidos,
        @Size(min = 8, max = 8) String dni,
        @Size(max = 30) String licencia,
        @Size(max = 20) String telefono
) {}
