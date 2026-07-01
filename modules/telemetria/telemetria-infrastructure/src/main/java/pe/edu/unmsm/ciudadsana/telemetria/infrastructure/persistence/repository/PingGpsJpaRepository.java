package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity.PingGpsJpaEntity;

import java.time.Instant;
import java.util.UUID;

public interface PingGpsJpaRepository extends JpaRepository<PingGpsJpaEntity, UUID> {

    Page<PingGpsJpaEntity> findAllByUnidadExternoIdAndTenantIdOrderByTsDesc(
            UUID unidadExternoId, UUID tenantId, Pageable pageable);

    @Query("SELECT p FROM PingGpsJpaEntity p WHERE p.tenantId = :tenantId " +
           "AND (:unidadId IS NULL OR p.unidadExternoId = :unidadId) " +
           "AND (:desde IS NULL OR p.ts >= :desde) " +
           "AND (:hasta IS NULL OR p.ts <= :hasta) " +
           "ORDER BY p.ts DESC")
    Page<PingGpsJpaEntity> findHistorico(@Param("tenantId") UUID tenantId,
                                         @Param("unidadId") UUID unidadId,
                                         @Param("desde") Instant desde,
                                         @Param("hasta") Instant hasta,
                                         Pageable pageable);
}
