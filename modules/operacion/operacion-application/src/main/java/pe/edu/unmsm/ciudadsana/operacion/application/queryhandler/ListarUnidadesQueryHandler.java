package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarUnidadesUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarUnidadesQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarUnidadesQueryHandler implements ListarUnidadesUseCase {

    private final UnidadesPersistencePort unidadesPersistencePort;

    public ListarUnidadesQueryHandler(UnidadesPersistencePort unidadesPersistencePort) {
        this.unidadesPersistencePort = unidadesPersistencePort;
    }

    @Override
    public Result<PageResult<UnidadResponseDto>> listar(ListarUnidadesQuery query) {
        PageResult<Unidad> pageResult = unidadesPersistencePort.findAll(TenantId.of(query.tenantId()), query.page(), query.size());
        PageResult<UnidadResponseDto> mappedPage = pageResult.map(u -> new UnidadResponseDto(
            u.getId().value(),
            u.getTenantId().value(),
            u.getPlaca().value(),
            u.getCodigoInterno(),
            u.getTipo().name(),
            u.getCapacidadM3().value(),
            u.getCapacidadKg().value(),
            u.getEstadoOperativo().name(),
            u.getCreadoEn()
        ));
        return Result.success(mappedPage);
    }
}
