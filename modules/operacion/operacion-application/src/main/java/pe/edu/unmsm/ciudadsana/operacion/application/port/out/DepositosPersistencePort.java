package pe.edu.unmsm.ciudadsana.operacion.application.port.out;

import pe.edu.unmsm.ciudadsana.operacion.domain.model.Deposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.util.List;
import java.util.Optional;

public interface DepositosPersistencePort {
    Optional<Deposito> findByIdAndTenantId(DepositoId id, TenantId tenantId);
    List<Deposito> findAllByTenantId(TenantId tenantId);
    long countByTenantId(TenantId tenantId);
    void save(Deposito deposito);
}
