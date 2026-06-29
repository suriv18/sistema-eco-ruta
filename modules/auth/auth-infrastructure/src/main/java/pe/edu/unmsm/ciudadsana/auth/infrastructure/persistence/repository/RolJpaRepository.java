package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.RolJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolJpaRepository extends JpaRepository<RolJpaEntity, UUID> {

    Optional<RolJpaEntity> findByCodigo(String codigo);

    List<RolJpaEntity> findAllByIdIn(List<UUID> ids);
}
