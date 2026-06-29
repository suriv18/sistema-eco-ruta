package pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.KpiUnidadJpaEntity;

import java.time.LocalDate;
import java.util.UUID;

public interface KpiUnidadJpaRepository extends JpaRepository<KpiUnidadJpaEntity, UUID> {

    @Query("SELECT e FROM KpiUnidadJpaEntity e WHERE e.tenantId = :tenantId " +
           "AND (:unidadId IS NULL OR e.unidadIdExterno = :unidadId) " +
           "AND (:fechaDesde IS NULL OR e.fecha >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR e.fecha <= :fechaHasta) " +
           "ORDER BY e.fecha DESC")
    Page<KpiUnidadJpaEntity> findAllFiltered(
            @Param("tenantId") UUID tenantId,
            @Param("unidadId") UUID unidadId,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable);
}
