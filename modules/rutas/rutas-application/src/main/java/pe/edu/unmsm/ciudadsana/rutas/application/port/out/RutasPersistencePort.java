package pe.edu.unmsm.ciudadsana.rutas.application.port.out;

import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface RutasPersistencePort {

    Optional<Ruta> findByIdAndTenantId(RutaId id, TenantId tenantId);

    PageResult<Ruta> findAllByTenantId(TenantId tenantId, UUID distritoId, LocalDate fecha, String estado, int page, int size);

    Ruta save(Ruta ruta);
}
