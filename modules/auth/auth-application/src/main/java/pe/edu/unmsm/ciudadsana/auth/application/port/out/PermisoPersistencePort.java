package pe.edu.unmsm.ciudadsana.auth.application.port.out;

import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.Optional;

public interface PermisoPersistencePort {
    Optional<Permiso> findById(PermisoId id);
    boolean existsByCodigo(String codigo);
    Permiso save(Permiso permiso);
    PageResult<Permiso> findAll(int page, int size);
}
