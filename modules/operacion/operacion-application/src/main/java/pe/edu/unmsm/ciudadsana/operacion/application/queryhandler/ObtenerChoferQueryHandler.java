package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerChoferUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerChoferQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ObtenerChoferQueryHandler implements ObtenerChoferUseCase {

    private final ChoferesPersistencePort choferesPersistencePort;

    public ObtenerChoferQueryHandler(ChoferesPersistencePort choferesPersistencePort) {
        this.choferesPersistencePort = choferesPersistencePort;
    }

    @Override
    public Result<ChoferResponseDto> obtener(ObtenerChoferQuery query) {
        return choferesPersistencePort.findById(ChoferId.of(query.id()), TenantId.of(query.tenantId()))
                .map(c -> Result.success(new ChoferResponseDto(
                        c.getId().value(),
                        c.getTenantId().value(),
                        c.getNombres(),
                        c.getApellidos(),
                        c.getDni().orElse(null),
                        c.getLicencia().orElse(null),
                        c.getTelefono().orElse(null),
                        c.getEstado().name(),
                        c.getCreadoEn()
                )))
                .orElse(Result.failure(ErrorCode.CHOFER_NO_ENCONTRADO));
    }
}
