package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarZonasUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarZonasQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarZonasQueryHandler implements ListarZonasUseCase {

    private final ZonasPersistencePort zonasPersistencePort;

    public ListarZonasQueryHandler(ZonasPersistencePort zonasPersistencePort) {
        this.zonasPersistencePort = zonasPersistencePort;
    }

    @Override
    public Result<PageResult<ZonaResponseDto>> listar(ListarZonasQuery query) {
        PageResult<Zona> pageResult = zonasPersistencePort.findAll(TenantId.of(query.tenantId()), query.page(), query.size());
        PageResult<ZonaResponseDto> mappedPage = pageResult.map(z -> new ZonaResponseDto(
            z.getId().value(),
            z.getTenantId().value(),
            z.getDistritoId().value(),
            z.getCodigo().value(),
            z.getNombre(),
            z.getTipo().name(),
            z.getPrioridad().value(),
            z.getEstado().name(),
            z.getCreadoEn()
        ));
        return Result.success(mappedPage);
    }
}
