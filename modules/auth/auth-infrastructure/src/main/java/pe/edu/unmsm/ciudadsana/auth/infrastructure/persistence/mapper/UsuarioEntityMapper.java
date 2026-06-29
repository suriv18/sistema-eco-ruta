package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.edu.unmsm.ciudadsana.auth.domain.enums.EstadoUsuario;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Email;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.NombresCompletos;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Username;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.RolJpaEntity;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.UsuarioJpaEntity;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UsuarioEntityMapper {

    default Usuario toDomain(UsuarioJpaEntity entity) {
        Set<RolId> roles = entity.getRoles().stream()
                .map(r -> RolId.of(r.getId()))
                .collect(Collectors.toSet());

        return Usuario.reconstitute(
                UsuarioId.of(entity.getId()),
                TenantId.of(entity.getTenantId()),
                NombresCompletos.of(entity.getNombres(), entity.getApellidos()),
                Email.of(entity.getEmail()),
                Username.of(entity.getUsername()),
                PasswordHash.of(entity.getPasswordHash()),
                entity.getTelefono(),
                EstadoUsuario.valueOf(entity.getEstado()),
                entity.getUltimoLoginEn(),
                entity.getCreadoEn(),
                entity.getActualizadoEn(),
                roles
        );
    }

    default UsuarioJpaEntity toEntity(Usuario domain, Set<RolJpaEntity> rolEntities) {
        UsuarioJpaEntity entity = new UsuarioJpaEntity();
        entity.setId(domain.getId().value());
        entity.setTenantId(domain.getTenantId().value());
        entity.setNombres(domain.getNombresCompletos().nombres());
        entity.setApellidos(domain.getNombresCompletos().apellidos());
        entity.setEmail(domain.getEmail().value());
        entity.setUsername(domain.getUsername().value());
        entity.setPasswordHash(domain.getPasswordHash().value());
        entity.setTelefono(domain.getTelefono());
        entity.setEstado(domain.getEstado().name());
        entity.setUltimoLoginEn(domain.getUltimoLoginEn());
        entity.setRoles(rolEntities);
        return entity;
    }

    default Rol rolToDomain(RolJpaEntity entity) {
        return Rol.reconstitute(
                RolId.of(entity.getId()),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getDescripcion(),
                "ACTIVO".equals(entity.getEstado())
        );
    }

    default RolJpaEntity rolToEntity(Rol rol) {
        RolJpaEntity entity = new RolJpaEntity();
        entity.setId(rol.getId().value());
        entity.setCodigo(rol.getCodigo());
        entity.setNombre(rol.getNombre());
        entity.setDescripcion(rol.getDescripcion());
        entity.setEstado(rol.isActivo() ? "ACTIVO" : "INACTIVO");
        return entity;
    }
}
