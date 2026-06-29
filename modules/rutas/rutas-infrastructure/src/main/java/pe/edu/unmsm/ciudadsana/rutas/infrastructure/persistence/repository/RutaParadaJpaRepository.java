package pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity.RutaParadaJpaEntity;

import java.util.List;
import java.util.UUID;

public interface RutaParadaJpaRepository extends JpaRepository<RutaParadaJpaEntity, UUID> {

    List<RutaParadaJpaEntity> findAllByRutaVersionIdOrderByOrdenAsc(UUID rutaVersionId);

    @Transactional
    void deleteAllByRutaVersionId(UUID rutaVersionId);
}
