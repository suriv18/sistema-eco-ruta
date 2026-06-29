package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerDistritoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerDistritoQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class ObtenerDistritoQueryHandler implements ObtenerDistritoUseCase {

    private final DistritosPersistencePort distritosPersistencePort;

    public ObtenerDistritoQueryHandler(DistritosPersistencePort distritosPersistencePort) {
        this.distritosPersistencePort = distritosPersistencePort;
    }

    @Override
    public Result<DistritoResponseDto> obtener(ObtenerDistritoQuery query) {
        Optional<Distrito> distritoOpt = distritosPersistencePort.findById(DistritoId.of(query.distritoId()), TenantId.of(query.tenantId()));
        if (distritoOpt.isEmpty()) return Result.failure(ErrorCode.DISTRITO_NO_ENCONTRADO);
        Distrito distrito = distritoOpt.get();
        return Result.success(new DistritoResponseDto(
            distrito.getId().value(),
            distrito.getTenantId().value(),
            distrito.getNombre(),
            distrito.getUbigeo().orElse(null),
            distrito.getEstado().name(),
            distrito.getCreadoEn()
        ));
    }
}
