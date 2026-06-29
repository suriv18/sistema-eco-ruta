package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.ZonaJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ZonaJpaRepository extends JpaRepository<ZonaJpaEntity, UUID> {
    boolean existsByCodigoAndTenantId(String codigo, UUID tenantId);
    Optional<ZonaJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<ZonaJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);
    List<ZonaJpaEntity> findAllByDistritoIdAndTenantId(UUID distritoId, UUID tenantId);
}
