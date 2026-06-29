package pe.edu.unmsm.ciudadsana.telemetria.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.PingGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

public interface PingGpsPersistencePort {

    PingGps save(PingGps ping);

    PageResult<PingGps> findAllByUnidad(UnidadExternoId unidadExternoId, TenantId tenantId, int page, int size);
}
