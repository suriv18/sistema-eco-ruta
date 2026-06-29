package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity.HorarioRecoleccionJpaEntity;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface HorarioRecoleccionJpaRepository extends JpaRepository<HorarioRecoleccionJpaEntity, UUID> {
    boolean existsByZonaIdAndDiaSemanaAndHoraInicioAndHoraFinAndTenantId(UUID zonaId, int diaSemana, LocalTime horaInicio, LocalTime horaFin, UUID tenantId);
    Optional<HorarioRecoleccionJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    Page<HorarioRecoleccionJpaEntity> findAllByZonaIdAndTenantId(UUID zonaId, UUID tenantId, Pageable pageable);
}
