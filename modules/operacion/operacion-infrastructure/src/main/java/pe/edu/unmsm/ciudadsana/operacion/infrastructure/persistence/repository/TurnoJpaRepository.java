package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.TurnoJpaEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface TurnoJpaRepository extends JpaRepository<TurnoJpaEntity, UUID> {

    Optional<TurnoJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Page<TurnoJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);

    @Query("""
            SELECT COUNT(t) > 0 FROM TurnoJpaEntity t
            WHERE t.tenantId = :tenantId
              AND t.unidadId = :unidadId
              AND t.fecha = :fecha
              AND t.estado NOT IN ('CANCELADO', 'FINALIZADO')
              AND t.horaInicio < :horaFin
              AND t.horaFin > :horaInicio
            """)
    boolean existsSuperposicionUnidad(
            @Param("tenantId") UUID tenantId,
            @Param("unidadId") UUID unidadId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin
    );

    @Query("""
            SELECT COUNT(t) > 0 FROM TurnoJpaEntity t
            WHERE t.tenantId = :tenantId
              AND t.choferId = :choferId
              AND t.fecha = :fecha
              AND t.estado NOT IN ('CANCELADO', 'FINALIZADO')
              AND t.horaInicio < :horaFin
              AND t.horaFin > :horaInicio
            """)
    boolean existsSuperposicionChofer(
            @Param("tenantId") UUID tenantId,
            @Param("choferId") UUID choferId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin
    );
}
