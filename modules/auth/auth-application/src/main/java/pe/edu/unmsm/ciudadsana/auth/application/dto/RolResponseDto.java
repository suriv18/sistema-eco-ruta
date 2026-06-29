package pe.edu.unmsm.ciudadsana.auth.application.dto;

import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;

import java.util.UUID;

public record RolResponseDto(
        UUID id,
        String codigo,
        String nombre,
        String descripcion,
        boolean activo
) {
    public static RolResponseDto from(Rol rol) {
        return new RolResponseDto(rol.getId().value(), rol.getCodigo(), rol.getNombre(), rol.getDescripcion(), rol.isActivo());
    }
}
