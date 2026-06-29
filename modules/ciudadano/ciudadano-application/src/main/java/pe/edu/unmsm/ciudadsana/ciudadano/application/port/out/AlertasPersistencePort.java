package pe.edu.unmsm.ciudadsana.ciudadano.application.port.out;

import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.Optional;

public interface AlertasPersistencePort {
    Optional<AlertaCiudadana> findByIdAndTenantId(AlertaId id, TenantId tenantId);
    PageResult<AlertaCiudadana> findAllByTenantId(TenantId tenantId, String estado, int page, int size);
    PageResult<AlertaCiudadana> findAllByZonaAndTenantId(ZonaExternoId zonaId, TenantId tenantId, int page, int size);
    PageResult<AlertaCiudadana> findCriticasByTenant(TenantId tenantId, int page, int size);
    AlertaCiudadana save(AlertaCiudadana alerta);
}
