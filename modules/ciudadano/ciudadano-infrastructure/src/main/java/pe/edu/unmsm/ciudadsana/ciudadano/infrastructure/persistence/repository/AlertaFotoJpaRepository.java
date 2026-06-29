package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.AlertaFotoJpaEntity;

import java.util.List;
import java.util.UUID;

public interface AlertaFotoJpaRepository extends JpaRepository<AlertaFotoJpaEntity, UUID> {

    List<AlertaFotoJpaEntity> findAllByAlertaId(UUID alertaId);
}
