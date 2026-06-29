package pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.ResumenOperativoDiarioJpaEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ResumenOperativoDiarioJpaRepository extends JpaRepository<ResumenOperativoDiarioJpaEntity, UUID> {

    Optional<ResumenOperativoDiarioJpaEntity> findByTenantIdAndDistritoIdExternoAndFecha(
            UUID tenantId, UUID distritoIdExterno, LocalDate fecha);
}
