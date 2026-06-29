package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.CiudadanoJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface CiudadanoJpaRepository extends JpaRepository<CiudadanoJpaEntity, UUID> {

    Optional<CiudadanoJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Page<CiudadanoJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);
}
