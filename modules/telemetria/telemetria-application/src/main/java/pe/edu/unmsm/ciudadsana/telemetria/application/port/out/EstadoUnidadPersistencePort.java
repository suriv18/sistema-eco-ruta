package pe.edu.unmsm.ciudadsana.telemetria.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.Optional;

public interface EstadoUnidadPersistencePort {

    Optional<EstadoUnidadView> findByUnidad(UnidadExternoId unidadExternoId, TenantId tenantId);

    void upsert(EstadoUnidadView estadoUnidad);

    PageResult<EstadoUnidadView> findAllByTenant(TenantId tenantId, int page, int size);

    record EstadoUnidadView(
            java.util.UUID id,
            java.util.UUID tenantId,
            java.util.UUID unidadExternoId,
            java.util.UUID rutaExternoId,
            double latitud,
            double longitud,
            Double ultimaVelocidadKmh,
            Instant ultimoPingEn,
            String estadoMovimiento,
            Instant actualizadoEn
    ) {}
}
