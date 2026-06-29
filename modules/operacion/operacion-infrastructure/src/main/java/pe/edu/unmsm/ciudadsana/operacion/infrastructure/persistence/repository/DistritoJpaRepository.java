package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.DistritoJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface DistritoJpaRepository extends JpaRepository<DistritoJpaEntity, UUID> {
    boolean existsByNombreAndTenantId(String nombre, UUID tenantId);
    Optional<DistritoJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<DistritoJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);
}
