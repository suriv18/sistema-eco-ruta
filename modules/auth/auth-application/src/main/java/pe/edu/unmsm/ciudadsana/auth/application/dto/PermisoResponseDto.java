package pe.edu.unmsm.ciudadsana.auth.application.dto;

import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;

import java.util.UUID;

public record PermisoResponseDto(
        UUID id,
        String codigo,
        String modulo,
        String descripcion
) {
    public static PermisoResponseDto from(Permiso permiso) {
        return new PermisoResponseDto(
                permiso.getId().value(),
                permiso.getCodigo(),
                permiso.getModulo(),
                permiso.getDescripcion()
        );
    }
}
