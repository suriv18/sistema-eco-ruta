package pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaEventoJpaEntity;

import java.util.List;
import java.util.UUID;

public interface RutaEventoJpaRepository extends JpaRepository<RutaEventoJpaEntity, UUID> {

    List<RutaEventoJpaEntity> findAllByRutaIdOrderByCreadoEnAsc(UUID rutaId);
}
