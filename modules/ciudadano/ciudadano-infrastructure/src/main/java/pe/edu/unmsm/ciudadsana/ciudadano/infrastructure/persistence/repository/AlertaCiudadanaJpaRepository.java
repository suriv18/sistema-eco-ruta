package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.AlertaCiudadanaJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface AlertaCiudadanaJpaRepository extends JpaRepository<AlertaCiudadanaJpaEntity, UUID> {

    Optional<AlertaCiudadanaJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Page<AlertaCiudadanaJpaEntity> findAllByTenantIdAndEstado(UUID tenantId, String estado, Pageable pageable);

    Page<AlertaCiudadanaJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);

    Page<AlertaCiudadanaJpaEntity> findAllByZonaIdExternoAndTenantId(UUID zonaIdExterno, UUID tenantId, Pageable pageable);

    Page<AlertaCiudadanaJpaEntity> findAllByTenantIdAndNivelCriticidad(UUID tenantId, String nivelCriticidad, Pageable pageable);
}
