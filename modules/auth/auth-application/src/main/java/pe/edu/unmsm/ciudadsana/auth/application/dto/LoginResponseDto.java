package pe.edu.unmsm.ciudadsana.auth.application.dto;

import java.util.Set;
import java.util.UUID;

public record LoginResponseDto(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UUID usuarioId,
        String username,
        Set<String> roles
) {}
