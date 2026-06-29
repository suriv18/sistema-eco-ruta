package pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.entity.EventoAuditoriaJpaEntity;

import java.time.Instant;
import java.util.UUID;

public interface EventoAuditoriaJpaRepository extends JpaRepository<EventoAuditoriaJpaEntity, UUID> {

    @Query("SELECT e FROM EventoAuditoriaJpaEntity e " +
           "WHERE e.tenantId = :tenantId " +
           "AND (:modulo IS NULL OR e.modulo = :modulo) " +
           "AND (:entidad IS NULL OR e.entidad = :entidad) " +
           "AND (:usuarioId IS NULL OR e.usuarioId = :usuarioId) " +
           "AND (:fechaDesde IS NULL OR e.creadoEn >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR e.creadoEn <= :fechaHasta) " +
           "ORDER BY e.creadoEn DESC")
    Page<EventoAuditoriaJpaEntity> findAllFiltered(
            @Param("tenantId") UUID tenantId,
            @Param("modulo") String modulo,
            @Param("entidad") String entidad,
            @Param("usuarioId") UUID usuarioId,
            @Param("fechaDesde") Instant fechaDesde,
            @Param("fechaHasta") Instant fechaHasta,
            Pageable pageable
    );
}
