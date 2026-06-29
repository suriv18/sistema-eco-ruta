package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DispositivoResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarDispositivosUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DispositivosPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarDispositivosQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarDispositivosQueryHandler implements ListarDispositivosUseCase {

    private final DispositivosPersistencePort dispositivosPersistencePort;

    public ListarDispositivosQueryHandler(DispositivosPersistencePort dispositivosPersistencePort) {
        this.dispositivosPersistencePort = dispositivosPersistencePort;
    }

    @Override
    public Result<PageResult<DispositivoResponseDto>> listar(ListarDispositivosQuery query) {
        PageResult<DispositivoGps> pageResult = dispositivosPersistencePort.findAllByTenantId(
                TenantId.of(query.tenantId()),
                query.page(),
                query.size()
        );
        return Result.success(pageResult.map(this::toDto));
    }

    private DispositivoResponseDto toDto(DispositivoGps d) {
        return new DispositivoResponseDto(
                d.getId().value(),
                d.getTenantId().value(),
                d.getUnidadExternoId().value(),
                d.getImei().orElse(null),
                d.getProveedor().orElse(null),
                d.getEstado().name(),
                d.getUltimoPingEn().orElse(null),
                d.getCreadoEn()
        );
    }
}
