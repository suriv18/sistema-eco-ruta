package pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaJpaEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface RutaJpaRepository extends JpaRepository<RutaJpaEntity, UUID> {

    Optional<RutaJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    @Query("SELECT r FROM RutaJpaEntity r WHERE r.tenantId = :tenantId " +
           "AND (:distritoId IS NULL OR r.distritoId = :distritoId) " +
           "AND (cast(:fecha as date) IS NULL OR r.fecha = :fecha) " +
           "AND (:estado IS NULL OR r.estado = :estado)")
    Page<RutaJpaEntity> findAllFiltered(
            @Param("tenantId") UUID tenantId,
            @Param("distritoId") UUID distritoId,
            @Param("fecha") LocalDate fecha,
            @Param("estado") String estado,
            Pageable pageable);
}
