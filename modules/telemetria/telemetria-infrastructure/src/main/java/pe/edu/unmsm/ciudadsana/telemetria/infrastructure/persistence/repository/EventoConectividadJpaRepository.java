package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.EventoConectividadJpaEntity;

import java.util.UUID;

public interface EventoConectividadJpaRepository extends JpaRepository<EventoConectividadJpaEntity, UUID> {

    Page<EventoConectividadJpaEntity> findAllByUnidadExternoIdAndTenantIdOrderByDetectadoEnDesc(
            UUID unidadExternoId, UUID tenantId, Pageable pageable);
}
