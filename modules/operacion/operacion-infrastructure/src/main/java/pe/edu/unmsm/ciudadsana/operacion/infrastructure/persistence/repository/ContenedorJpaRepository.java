package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.ContenedorJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContenedorJpaRepository extends JpaRepository<ContenedorJpaEntity, UUID> {
    boolean existsByCodigoAndTenantId(String codigo, UUID tenantId);
    Optional<ContenedorJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<ContenedorJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);
    List<ContenedorJpaEntity> findAllByZonaIdAndTenantId(UUID zonaId, UUID tenantId);
}
