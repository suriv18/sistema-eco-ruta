package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.PingGpsResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarHistoricoPingsUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.PingGpsPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.HistoricoPingsQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.PingGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;

@Component
public class ListarHistoricoPingsQueryHandler implements ListarHistoricoPingsUseCase {

    private final PingGpsPersistencePort port;

    public ListarHistoricoPingsQueryHandler(PingGpsPersistencePort port) {
        this.port = port;
    }

    @Override
    public Result<PageResult<PingGpsResponseDto>> listar(HistoricoPingsQuery q) {
        UnidadExternoId unidadId = q.unidadExternoId() != null ? UnidadExternoId.of(q.unidadExternoId()) : null;
        PageResult<PingGps> page = port.findHistorico(
                TenantId.of(q.tenantId()), unidadId, q.desde(), q.hasta(), q.page(), q.size());
        return Result.success(page.map(p -> new PingGpsResponseDto(
                p.getId().value(),
                p.getTenantId().value(),
                p.getDispositivoId().value(),
                p.getUnidadExternoId().value(),
                p.getRutaExternoId().map(RutaExternoId::value).orElse(null),
                p.getTs(),
                p.getPosicion().latitud(),
                p.getPosicion().longitud(),
                p.getVelocidadKmh().orElse(null),
                p.getRumboGrados().orElse(null),
                p.getPrecisionM().orElse(null),
                p.getOrigen().name(),
                p.getRecibidoEn()
        )));
    }
}
