package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.DispositivoGpsJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface DispositivoGpsJpaRepository extends JpaRepository<DispositivoGpsJpaEntity, UUID> {

    boolean existsByUnidadExternoIdAndTenantId(UUID unidadExternoId, UUID tenantId);

    Optional<DispositivoGpsJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<DispositivoGpsJpaEntity> findByUnidadExternoIdAndTenantId(UUID unidadExternoId, UUID tenantId);

    Page<DispositivoGpsJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);
}
