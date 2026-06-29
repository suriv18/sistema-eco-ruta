package pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.KpiAlertaJpaEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public interface KpiAlertaJpaRepository extends JpaRepository<KpiAlertaJpaEntity, UUID> {

    @Query("SELECT e FROM KpiAlertaJpaEntity e WHERE e.tenantId = :tenantId " +
           "AND (:zonaId IS NULL OR e.zonaIdExterno = :zonaId) " +
           "AND (:fechaDesde IS NULL OR CAST(e.registradaEn AS java.time.LocalDate) >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR CAST(e.registradaEn AS java.time.LocalDate) <= :fechaHasta) " +
           "ORDER BY e.registradaEn DESC")
    Page<KpiAlertaJpaEntity> findAllFiltered(
            @Param("tenantId") UUID tenantId,
            @Param("zonaId") UUID zonaId,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable);
}
