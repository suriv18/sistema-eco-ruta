package pe.edu.unmsm.ciudadsana.operacion.application.port.out;

import pe.edu.unmsm.ciudadsana.operacion.domain.model.Contenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

public interface ContenedoresPersistencePort {
    Optional<Contenedor> findById(ContenedorId id, TenantId tenantId);
    boolean existsByCodigo(String codigo, TenantId tenantId);
    Contenedor save(Contenedor contenedor);
    PageResult<Contenedor> findAll(TenantId tenantId, int page, int size);
    List<Contenedor> findByZona(ZonaId zonaId, TenantId tenantId);
}
