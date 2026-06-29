package pe.edu.unmsm.ciudadsana.ciudadano.application.port.out;

import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.Ciudadano;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.Optional;

public interface CiudadanosPersistencePort {
    Optional<Ciudadano> findByIdAndTenantId(CiudadanoId id, TenantId tenantId);
    PageResult<Ciudadano> findAllByTenantId(TenantId tenantId, int page, int size);
    Ciudadano save(Ciudadano ciudadano);
}
