package pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaVersionJpaEntity;

import java.util.List;
import java.util.UUID;

public interface RutaVersionJpaRepository extends JpaRepository<RutaVersionJpaEntity, UUID> {

    List<RutaVersionJpaEntity> findAllByRutaIdOrderByVersionAsc(UUID rutaId);
}
