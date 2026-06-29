package pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.entity.OutboxEventJpaEntity;

import java.util.List;
import java.util.UUID;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventJpaEntity, UUID> {

    @Query("SELECT e FROM OutboxEventJpaEntity e " +
           "WHERE e.tenantId = :tenantId " +
           "AND (:estado IS NULL OR e.estado = :estado) " +
           "AND (:eventType IS NULL OR e.eventType = :eventType) " +
           "ORDER BY e.creadoEn DESC")
    Page<OutboxEventJpaEntity> findAllFiltered(
            @Param("tenantId") UUID tenantId,
            @Param("estado") String estado,
            @Param("eventType") String eventType,
            Pageable pageable
    );

    List<OutboxEventJpaEntity> findByEstadoOrderByCreadoEnAsc(String estado);
}
