package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.UnidadJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnidadJpaRepository extends JpaRepository<UnidadJpaEntity, UUID> {
    boolean existsByPlacaAndTenantId(String placa, UUID tenantId);
    Optional<UnidadJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<UnidadJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);
    List<UnidadJpaEntity> findAllByTenantIdAndEstadoOperativo(UUID tenantId, String estadoOperativo);
}
