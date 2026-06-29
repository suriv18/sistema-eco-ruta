package pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.KpiRutaJpaEntity;

import java.time.LocalDate;
import java.util.UUID;

public interface KpiRutaJpaRepository extends JpaRepository<KpiRutaJpaEntity, UUID> {

    @Query("SELECT e FROM KpiRutaJpaEntity e WHERE e.tenantId = :tenantId " +
           "AND (:fechaDesde IS NULL OR e.fecha >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR e.fecha <= :fechaHasta) " +
           "ORDER BY e.fecha DESC")
    Page<KpiRutaJpaEntity> findAllFiltered(
            @Param("tenantId") UUID tenantId,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable);
}
