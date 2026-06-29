package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.PermisoJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface PermisoJpaRepository extends JpaRepository<PermisoJpaEntity, UUID> {

    Optional<PermisoJpaEntity> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
