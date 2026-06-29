package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.DesvioRutaJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface DesvioRutaJpaRepository extends JpaRepository<DesvioRutaJpaEntity, UUID> {

    Optional<DesvioRutaJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Page<DesvioRutaJpaEntity> findAllByRutaExternoIdAndTenantIdAndEstado(
            UUID rutaExternoId, UUID tenantId, String estado, Pageable pageable);
}
