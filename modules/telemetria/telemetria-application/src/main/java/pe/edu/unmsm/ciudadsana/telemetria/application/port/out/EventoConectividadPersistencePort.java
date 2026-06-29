package pe.edu.unmsm.ciudadsana.telemetria.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.UUID;

public interface EventoConectividadPersistencePort {

    EventoConectividadView save(EventoConectividadView evento);

    PageResult<EventoConectividadView> findAllByUnidad(UnidadExternoId unidadExternoId, TenantId tenantId, int page, int size);

    record EventoConectividadView(
            UUID id,
            UUID tenantId,
            UUID unidadExternoId,
            UUID dispositivoId,
            String tipoEvento,
            String detalle,
            Instant detectadoEn
    ) {}
}
