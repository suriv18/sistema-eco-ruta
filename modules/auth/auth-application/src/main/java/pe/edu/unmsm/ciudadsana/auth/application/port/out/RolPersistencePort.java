package pe.edu.unmsm.ciudadsana.auth.application.port.out;

import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;

import java.util.List;
import java.util.Optional;

public interface RolPersistencePort {
    Optional<Rol> findById(RolId id);
    Optional<Rol> findByCodigo(String codigo);
    List<Rol> findByIds(List<RolId> ids);
}
