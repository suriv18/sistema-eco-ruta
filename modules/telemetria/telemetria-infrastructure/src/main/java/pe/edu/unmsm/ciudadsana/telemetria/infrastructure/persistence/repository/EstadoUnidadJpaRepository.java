package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.EstadoUnidadJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface EstadoUnidadJpaRepository extends JpaRepository<EstadoUnidadJpaEntity, UUID> {

    Optional<EstadoUnidadJpaEntity> findByUnidadExternoIdAndTenantId(UUID unidadExternoId, UUID tenantId);

    Page<EstadoUnidadJpaEntity> findByTenantId(UUID tenantId, Pageable pageable);
}
