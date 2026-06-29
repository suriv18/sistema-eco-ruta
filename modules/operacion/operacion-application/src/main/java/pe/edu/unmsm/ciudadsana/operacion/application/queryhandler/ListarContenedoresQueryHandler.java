package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarContenedoresUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ContenedoresPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarContenedoresQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Contenedor;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarContenedoresQueryHandler implements ListarContenedoresUseCase {

    private final ContenedoresPersistencePort contenedoresPersistencePort;

    public ListarContenedoresQueryHandler(ContenedoresPersistencePort contenedoresPersistencePort) {
        this.contenedoresPersistencePort = contenedoresPersistencePort;
    }

    @Override
    public Result<PageResult<ContenedorResponseDto>> listar(ListarContenedoresQuery query) {
        PageResult<Contenedor> pageResult = contenedoresPersistencePort.findAll(TenantId.of(query.tenantId()), query.page(), query.size());
        PageResult<ContenedorResponseDto> mappedPage = pageResult.map(c -> new ContenedorResponseDto(
            c.getId().value(),
            c.getTenantId().value(),
            c.getZonaId().value(),
            c.getCodigo(),
            c.getCapacidad().value(),
            c.getEstadoContenedor().name(),
            c.getCreadoEn()
        ));
        return Result.success(mappedPage);
    }
}
