package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity.ValidacionAlertaJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface ValidacionAlertaJpaRepository extends JpaRepository<ValidacionAlertaJpaEntity, UUID> {

    Optional<ValidacionAlertaJpaEntity> findByAlertaId(UUID alertaId);
}
