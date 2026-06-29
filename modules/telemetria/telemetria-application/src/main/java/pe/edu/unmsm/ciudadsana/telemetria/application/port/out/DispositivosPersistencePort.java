package pe.edu.unmsm.ciudadsana.telemetria.application.port.out;

import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.Optional;

public interface DispositivosPersistencePort {
    boolean existsByUnidadExternoIdAndTenantId(UnidadExternoId id, TenantId tenantId);
    Optional<DispositivoGps> findByIdAndTenantId(DispositivoId id, TenantId tenantId);
    Optional<DispositivoGps> findByUnidadExternoIdAndTenantId(UnidadExternoId id, TenantId tenantId);
    PageResult<DispositivoGps> findAllByTenantId(TenantId tenantId, int page, int size);
    DispositivoGps save(DispositivoGps dispositivo);
}
