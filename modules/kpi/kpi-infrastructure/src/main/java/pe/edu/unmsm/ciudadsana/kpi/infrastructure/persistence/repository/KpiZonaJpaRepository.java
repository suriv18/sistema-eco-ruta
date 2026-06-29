package pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity.KpiZonaJpaEntity;

import java.time.LocalDate;
import java.util.UUID;

public interface KpiZonaJpaRepository extends JpaRepository<KpiZonaJpaEntity, UUID> {

    @Query("SELECT e FROM KpiZonaJpaEntity e WHERE e.tenantId = :tenantId " +
           "AND (:zonaId IS NULL OR e.zonaIdExterno = :zonaId) " +
           "AND (:fechaDesde IS NULL OR e.fecha >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR e.fecha <= :fechaHasta) " +
           "ORDER BY e.fecha DESC")
    Page<KpiZonaJpaEntity> findAllFiltered(
            @Param("tenantId") UUID tenantId,
            @Param("zonaId") UUID zonaId,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable);
}
