package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EstadoUnidadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarEstadosUnidadesUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarEstadosUnidadesQuery;

@Component
public class ListarEstadosUnidadesQueryHandler implements ListarEstadosUnidadesUseCase {

    private final EstadoUnidadPersistencePort port;

    public ListarEstadosUnidadesQueryHandler(EstadoUnidadPersistencePort port) {
        this.port = port;
    }

    @Override
    public Result<PageResult<EstadoUnidadResponseDto>> listar(ListarEstadosUnidadesQuery q) {
        return Result.success(port.findAllByTenant(TenantId.of(q.tenantId()), q.page(), q.size())
                .map(e -> new EstadoUnidadResponseDto(
                        e.unidadExternoId(),
                        e.tenantId(),
                        e.rutaExternoId(),
                        e.latitud(),
                        e.longitud(),
                        e.ultimaVelocidadKmh(),
                        e.ultimoPingEn(),
                        e.estadoMovimiento(),
                        e.actualizadoEn())));
    }
}
