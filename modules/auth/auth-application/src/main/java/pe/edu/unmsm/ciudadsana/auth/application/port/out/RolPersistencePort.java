package pe.edu.unmsm.ciudadsana.auth.application.port.out;

import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolPersistencePort {
    Optional<Rol> findById(RolId id);
    Optional<Rol> findByCodigo(String codigo);
    List<Rol> findByIds(List<RolId> ids);
    Rol save(Rol rol);
    PageResult<Rol> findAll(int page, int size);
    boolean existsByCodigo(String codigo);
    void asignarPermiso(UUID rolId, UUID permisoId);
    void quitarPermiso(UUID rolId, UUID permisoId);
    boolean tienePermiso(UUID rolId, UUID permisoId);
}
