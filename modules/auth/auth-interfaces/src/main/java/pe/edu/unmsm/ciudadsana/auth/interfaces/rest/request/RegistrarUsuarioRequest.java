package pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RegistrarUsuarioRequest(
        @NotNull UUID tenantId,
        @NotBlank @Size(max = 120) String nombres,
        @NotBlank @Size(max = 120) String apellidos,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(min = 3, max = 80) String username,
        @NotBlank @Size(min = 8, max = 100) String password,
        String telefono
) {}
