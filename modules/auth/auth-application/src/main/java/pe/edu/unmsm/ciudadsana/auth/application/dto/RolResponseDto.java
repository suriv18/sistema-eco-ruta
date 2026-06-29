package pe.edu.unmsm.ciudadsana.auth.application.dto;

import java.util.UUID;

public record RolResponseDto(
        UUID id,
        String codigo,
        String nombre,
        String descripcion,
        boolean activo
) {}
