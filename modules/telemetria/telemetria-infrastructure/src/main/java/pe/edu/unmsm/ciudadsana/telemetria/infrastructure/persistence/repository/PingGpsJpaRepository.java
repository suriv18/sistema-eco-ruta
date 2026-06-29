package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.PingGpsJpaEntity;

import java.util.UUID;

public interface PingGpsJpaRepository extends JpaRepository<PingGpsJpaEntity, UUID> {

    Page<PingGpsJpaEntity> findAllByUnidadExternoIdAndTenantIdOrderByTsDesc(
            UUID unidadExternoId, UUID tenantId, Pageable pageable);
}
