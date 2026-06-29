package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DispositivoResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ObtenerDispositivoUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DispositivosPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ObtenerDispositivoQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class ObtenerDispositivoQueryHandler implements ObtenerDispositivoUseCase {

    private final DispositivosPersistencePort dispositivosPersistencePort;

    public ObtenerDispositivoQueryHandler(DispositivosPersistencePort dispositivosPersistencePort) {
        this.dispositivosPersistencePort = dispositivosPersistencePort;
    }

    @Override
    public Result<DispositivoResponseDto> obtener(ObtenerDispositivoQuery query) {
        Optional<DispositivoGps> dispositivoOpt = dispositivosPersistencePort.findByIdAndTenantId(
                DispositivoId.of(query.dispositivoId()),
                TenantId.of(query.tenantId())
        );
        if (dispositivoOpt.isEmpty()) {
            return Result.failure(ErrorCode.RECURSO_NO_ENCONTRADO);
        }
        return Result.success(toDto(dispositivoOpt.get()));
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
