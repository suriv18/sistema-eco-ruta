package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.UsuarioJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, UUID> {

    Optional<UsuarioJpaEntity> findByUsernameAndTenantId(String username, UUID tenantId);

    Optional<UsuarioJpaEntity> findByEmailAndTenantId(String email, UUID tenantId);

    boolean existsByEmailAndTenantId(String email, UUID tenantId);

    boolean existsByUsernameAndTenantId(String username, UUID tenantId);

    Page<UsuarioJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);
}
