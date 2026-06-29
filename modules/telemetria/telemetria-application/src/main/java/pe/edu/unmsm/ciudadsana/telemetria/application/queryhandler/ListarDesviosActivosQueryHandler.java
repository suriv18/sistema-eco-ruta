package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DesvioRutaResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarDesviosActivosUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort.DesvioView;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarDesviosActivosQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.RutaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarDesviosActivosQueryHandler implements ListarDesviosActivosUseCase {

    private final DesvioRutaPersistencePort desvioRutaPersistencePort;

    public ListarDesviosActivosQueryHandler(DesvioRutaPersistencePort desvioRutaPersistencePort) {
        this.desvioRutaPersistencePort = desvioRutaPersistencePort;
    }

    @Override
    public Result<PageResult<DesvioRutaResponseDto>> listar(ListarDesviosActivosQuery query) {
        PageResult<DesvioView> pageResult = desvioRutaPersistencePort.findActivosByRuta(
                RutaExternoId.of(query.rutaExternoId()),
                TenantId.of(query.tenantId()),
                query.page(),
                query.size()
        );
        return Result.success(pageResult.map(this::toDto));
    }

    private DesvioRutaResponseDto toDto(DesvioView d) {
        return new DesvioRutaResponseDto(
                d.id(),
                d.tenantId(),
                d.unidadExternoId(),
                d.rutaExternoId(),
                d.latitud(),
                d.longitud(),
                d.distanciaDesvioM(),
                d.severidad(),
                d.estado(),
                d.detectadoEn(),
                d.atendidoEn()
        );
    }
}
