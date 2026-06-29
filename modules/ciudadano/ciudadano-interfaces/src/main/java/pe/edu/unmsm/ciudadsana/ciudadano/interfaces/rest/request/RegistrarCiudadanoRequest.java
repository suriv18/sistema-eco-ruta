package pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegistrarCiudadanoRequest(
        @Size(max = 120) String nombres,
        @Size(max = 120) String apellidos,
        @Email @Size(max = 150) String email,
        @Size(max = 20) String telefono,
        @Size(max = 20) String documento
) {}
