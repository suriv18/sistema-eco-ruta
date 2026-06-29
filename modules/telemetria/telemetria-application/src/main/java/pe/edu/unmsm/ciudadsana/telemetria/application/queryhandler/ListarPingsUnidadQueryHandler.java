package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.PingGpsResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarPingsUnidadUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.PingGpsPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarPingsUnidadQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.PingGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarPingsUnidadQueryHandler implements ListarPingsUnidadUseCase {

    private final PingGpsPersistencePort pingGpsPersistencePort;

    public ListarPingsUnidadQueryHandler(PingGpsPersistencePort pingGpsPersistencePort) {
        this.pingGpsPersistencePort = pingGpsPersistencePort;
    }

    @Override
    public Result<PageResult<PingGpsResponseDto>> listar(ListarPingsUnidadQuery query) {
        PageResult<PingGps> pageResult = pingGpsPersistencePort.findAllByUnidad(
                UnidadExternoId.of(query.unidadExternoId()),
                TenantId.of(query.tenantId()),
                query.page(),
                query.size()
        );
        return Result.success(pageResult.map(this::toDto));
    }

    private PingGpsResponseDto toDto(PingGps p) {
        return new PingGpsResponseDto(
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
        );
    }
}
