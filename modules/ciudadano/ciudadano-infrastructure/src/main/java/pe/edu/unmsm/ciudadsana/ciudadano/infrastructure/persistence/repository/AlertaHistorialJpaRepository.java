package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.AlertaHistorialJpaEntity;

import java.util.List;
import java.util.UUID;

public interface AlertaHistorialJpaRepository extends JpaRepository<AlertaHistorialJpaEntity, UUID> {

    List<AlertaHistorialJpaEntity> findAllByAlertaIdOrderByCambiadoEnAsc(UUID alertaId);
}
