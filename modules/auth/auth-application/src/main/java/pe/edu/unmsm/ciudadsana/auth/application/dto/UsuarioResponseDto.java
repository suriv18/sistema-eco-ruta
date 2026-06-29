package pe.edu.unmsm.ciudadsana.auth.application.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UsuarioResponseDto(
        UUID usuarioId,
        UUID tenantId,
        String nombres,
        String apellidos,
        String email,
        String username,
        String telefono,
        String estado,
        Set<String> roles,
        Instant creadoEn
) {}
