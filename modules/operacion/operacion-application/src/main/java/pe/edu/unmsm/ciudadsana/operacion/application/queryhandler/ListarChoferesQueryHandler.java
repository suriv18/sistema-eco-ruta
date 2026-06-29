package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarChoferesUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarChoferesQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarChoferesQueryHandler implements ListarChoferesUseCase {

    private final ChoferesPersistencePort choferesPersistencePort;

    public ListarChoferesQueryHandler(ChoferesPersistencePort choferesPersistencePort) {
        this.choferesPersistencePort = choferesPersistencePort;
    }

    @Override
    public Result<PageResult<ChoferResponseDto>> listar(ListarChoferesQuery query) {
        PageResult<Chofer> pageResult = choferesPersistencePort.findAll(TenantId.of(query.tenantId()), query.page(), query.size());
        PageResult<ChoferResponseDto> mappedPage = pageResult.map(c -> new ChoferResponseDto(
            c.getId().value(),
            c.getTenantId().value(),
            c.getNombres(),
            c.getApellidos(),
            c.getDni().orElse(null),
            c.getLicencia().orElse(null),
            c.getTelefono().orElse(null),
            c.getEstado().name(),
            c.getCreadoEn()
        ));
        return Result.success(mappedPage);
    }
}
