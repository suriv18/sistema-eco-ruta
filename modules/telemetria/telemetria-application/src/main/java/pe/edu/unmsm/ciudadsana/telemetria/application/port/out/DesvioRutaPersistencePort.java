package pe.edu.unmsm.ciudadsana.telemetria.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DesvioId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface DesvioRutaPersistencePort {

    Optional<DesvioView> findById(DesvioId id, TenantId tenantId);

    DesvioView save(DesvioView desvio);

    PageResult<DesvioView> findActivosByRuta(RutaExternoId rutaExternoId, TenantId tenantId, int page, int size);

    record DesvioView(
            UUID id,
            UUID tenantId,
            UUID unidadExternoId,
            UUID rutaExternoId,
            double latitud,
            double longitud,
            double distanciaDesvioM,
            String severidad,
            String estado,
            Instant detectadoEn,
            Instant atendidoEn
    ) {}
}
