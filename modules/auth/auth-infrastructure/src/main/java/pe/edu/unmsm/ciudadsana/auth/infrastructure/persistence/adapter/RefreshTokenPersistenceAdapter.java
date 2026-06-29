package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RefreshTokenPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.RefreshToken;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.mapper.RefreshTokenEntityMapper;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository.RefreshTokenJpaRepository;

import java.time.Instant;
import java.util.Optional;

@Component
public class RefreshTokenPersistenceAdapter implements RefreshTokenPersistencePort {

    private final RefreshTokenJpaRepository repo;
    private final RefreshTokenEntityMapper mapper;

    public RefreshTokenPersistenceAdapter(RefreshTokenJpaRepository repo, RefreshTokenEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public RefreshToken save(RefreshToken domain) {
        RefreshTokenJpaEntity entity = mapper.toEntity(domain);
        return mapper.toDomain(repo.save(entity));
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return repo.findByTokenHash(tokenHash).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void revokeAllByUsuarioId(UsuarioId usuarioId) {
        repo.revokeAllByUsuarioId(usuarioId.value(), Instant.now());
    }
}
