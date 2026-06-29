package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.DepositoJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepositoJpaRepository extends JpaRepository<DepositoJpaEntity, UUID> {
    Optional<DepositoJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<DepositoJpaEntity> findAllByTenantId(UUID tenantId);
    long countByTenantId(UUID tenantId);
}
