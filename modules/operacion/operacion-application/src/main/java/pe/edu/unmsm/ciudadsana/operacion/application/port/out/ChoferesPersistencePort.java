package pe.edu.unmsm.ciudadsana.operacion.application.port.out;

import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.util.Optional;

public interface ChoferesPersistencePort {
    Optional<Chofer> findById(ChoferId id, TenantId tenantId);
    boolean existsByDni(String dni, TenantId tenantId);
    Chofer save(Chofer chofer);
    PageResult<Chofer> findAll(TenantId tenantId, int page, int size);
}
