package pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotNull UUID tenantId
) {}
