package pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) {}
