package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.LoginAuditoriaJpaEntity;

import java.util.UUID;

public interface LoginAuditoriaJpaRepository extends JpaRepository<LoginAuditoriaJpaEntity, UUID> {
}
