package pe.edu.unmsm.ciudadsana.operacion.application.port.out;

import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.Placa;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

public interface UnidadesPersistencePort {
    Optional<Unidad> findById(UnidadId id, TenantId tenantId);
    boolean existsByPlaca(Placa placa, TenantId tenantId);
    Unidad save(Unidad unidad);
    PageResult<Unidad> findAll(TenantId tenantId, int page, int size);
    List<Unidad> findDisponibles(TenantId tenantId);
}
