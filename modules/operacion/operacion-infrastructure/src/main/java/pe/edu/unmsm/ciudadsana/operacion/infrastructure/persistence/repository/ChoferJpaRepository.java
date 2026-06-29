package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.ChoferJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface ChoferJpaRepository extends JpaRepository<ChoferJpaEntity, UUID> {
    boolean existsByDniAndTenantId(String dni, UUID tenantId);
    Optional<ChoferJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<ChoferJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);
}
