package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import pe.edu.unmsm.ciudadsana.auth.domain.model.RefreshToken;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RefreshTokenId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.RefreshTokenJpaEntity;

@Mapper(componentModel = "spring")
public interface RefreshTokenEntityMapper {

    default RefreshToken toDomain(RefreshTokenJpaEntity entity) {
        return RefreshToken.reconstitute(
                RefreshTokenId.of(entity.getId()),
                UsuarioId.of(entity.getUsuarioId()),
                entity.getTokenHash(),
                entity.getExpiraEn(),
                entity.isRevocado(),
                entity.getCreadoEn(),
                entity.getRevocadoEn()
        );
    }

    default RefreshTokenJpaEntity toEntity(RefreshToken domain) {
        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setId(domain.getId().value());
        entity.setUsuarioId(domain.getUsuarioId().value());
        entity.setTokenHash(domain.getTokenHash());
        entity.setExpiraEn(domain.getExpiraEn());
        entity.setRevocado(domain.isRevocado());
        entity.setRevocadoEn(domain.getRevocadoEn());
        return entity;
    }
}
