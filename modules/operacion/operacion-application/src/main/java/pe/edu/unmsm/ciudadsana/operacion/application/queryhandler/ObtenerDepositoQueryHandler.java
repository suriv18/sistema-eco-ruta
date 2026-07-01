package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerDepositoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DepositosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerDepositoQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ObtenerDepositoQueryHandler implements ObtenerDepositoUseCase {

    private final DepositosPersistencePort depositosPersistencePort;

    public ObtenerDepositoQueryHandler(DepositosPersistencePort depositosPersistencePort) {
        this.depositosPersistencePort = depositosPersistencePort;
    }

    @Override
    public Result<DepositoResponseDto> obtener(ObtenerDepositoQuery query) {
        return depositosPersistencePort.findByIdAndTenantId(DepositoId.of(query.id()), TenantId.of(query.tenantId()))
                .map(d -> Result.success(new DepositoResponseDto(
                        d.getId().value(),
                        d.getTenantId().value(),
                        d.getDistritoId().value(),
                        d.getNombre(),
                        d.getTipo().name(),
                        d.getEstado().name(),
                        d.getCreadoEn()
                )))
                .orElse(Result.failure(ErrorCode.DEPOSITO_NO_ENCONTRADO));
    }
}
