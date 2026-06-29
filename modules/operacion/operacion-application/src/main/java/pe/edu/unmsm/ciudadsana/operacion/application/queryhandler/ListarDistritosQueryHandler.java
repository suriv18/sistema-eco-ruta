package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarDistritosUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarDistritosQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarDistritosQueryHandler implements ListarDistritosUseCase {

    private final DistritosPersistencePort distritosPersistencePort;

    public ListarDistritosQueryHandler(DistritosPersistencePort distritosPersistencePort) {
        this.distritosPersistencePort = distritosPersistencePort;
    }

    @Override
    public Result<PageResult<DistritoResponseDto>> listar(ListarDistritosQuery query) {
        PageResult<Distrito> pageResult = distritosPersistencePort.findAll(TenantId.of(query.tenantId()), query.page(), query.size());
        PageResult<DistritoResponseDto> mappedPage = pageResult.map(d -> new DistritoResponseDto(
            d.getId().value(),
            d.getTenantId().value(),
            d.getNombre(),
            d.getUbigeo().orElse(null),
            d.getEstado().name(),
            d.getCreadoEn()
        ));
        return Result.success(mappedPage);
    }
}
