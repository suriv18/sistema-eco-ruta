package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ListarCiudadanosUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanosPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarCiudadanosQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.Ciudadano;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarCiudadanosQueryHandler implements ListarCiudadanosUseCase {

    private final CiudadanosPersistencePort ciudadanosPersistencePort;

    public ListarCiudadanosQueryHandler(CiudadanosPersistencePort ciudadanosPersistencePort) {
        this.ciudadanosPersistencePort = ciudadanosPersistencePort;
    }

    @Override
    public Result<PageResult<CiudadanoResponseDto>> listar(ListarCiudadanosQuery query) {
        TenantId tenantId = TenantId.of(query.tenantId());
        PageResult<Ciudadano> pageResult = ciudadanosPersistencePort.findAllByTenantId(tenantId, query.page(), query.size());
        return Result.success(pageResult.map(this::toDto));
    }

    private CiudadanoResponseDto toDto(Ciudadano c) {
        return new CiudadanoResponseDto(
            c.getId().value(), c.getTenantId().value(),
            c.getNombres().orElse(null), c.getApellidos().orElse(null),
            c.getEmail().orElse(null), c.getTelefono().orElse(null),
            c.getDocumento().orElse(null), c.getEstado().name(), c.getCreadoEn()
        );
    }
}
