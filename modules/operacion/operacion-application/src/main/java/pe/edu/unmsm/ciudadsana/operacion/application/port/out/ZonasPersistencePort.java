package pe.edu.unmsm.ciudadsana.operacion.application.port.out;

import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CodigoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.List;
import java.util.Optional;

public interface ZonasPersistencePort {
    Optional<Zona> findById(ZonaId id, TenantId tenantId);
    boolean existsByCodigo(CodigoZona codigo, TenantId tenantId);
    List<Zona> findByDistrito(DistritoId distritoId, TenantId tenantId);
    Zona save(Zona zona);
    PageResult<Zona> findAll(TenantId tenantId, int page, int size);
}
