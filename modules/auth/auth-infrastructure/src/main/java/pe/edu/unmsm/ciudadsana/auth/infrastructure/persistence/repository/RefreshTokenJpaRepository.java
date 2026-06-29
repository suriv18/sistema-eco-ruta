package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.RefreshTokenJpaEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, UUID> {

    Optional<RefreshTokenJpaEntity> findByTokenHash(String tokenHash);

    @Modifying
    @Query("UPDATE RefreshTokenJpaEntity r SET r.revocado = true, r.revocadoEn = :ahora WHERE r.usuarioId = :usuarioId AND r.revocado = false")
    void revokeAllByUsuarioId(UUID usuarioId, Instant ahora);
}
