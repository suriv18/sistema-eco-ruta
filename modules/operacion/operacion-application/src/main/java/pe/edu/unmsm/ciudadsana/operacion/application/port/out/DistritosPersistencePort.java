package pe.edu.unmsm.ciudadsana.operacion.application.port.out;

import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.Optional;

public interface DistritosPersistencePort {
    Optional<Distrito> findById(DistritoId id, TenantId tenantId);
    boolean existsByNombre(String nombre, TenantId tenantId);
    Distrito save(Distrito distrito);
    PageResult<Distrito> findAll(TenantId tenantId, int page, int size);
}
