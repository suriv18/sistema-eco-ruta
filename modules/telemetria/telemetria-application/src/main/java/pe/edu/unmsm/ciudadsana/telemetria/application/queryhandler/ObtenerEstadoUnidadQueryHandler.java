package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EstadoUnidadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ObtenerEstadoUnidadUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort.EstadoUnidadView;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ObtenerEstadoUnidadQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class ObtenerEstadoUnidadQueryHandler implements ObtenerEstadoUnidadUseCase {

    private final EstadoUnidadPersistencePort estadoUnidadPersistencePort;

    public ObtenerEstadoUnidadQueryHandler(EstadoUnidadPersistencePort estadoUnidadPersistencePort) {
        this.estadoUnidadPersistencePort = estadoUnidadPersistencePort;
    }

    @Override
    public Result<EstadoUnidadResponseDto> obtener(ObtenerEstadoUnidadQuery query) {
        Optional<EstadoUnidadView> estadoOpt = estadoUnidadPersistencePort.findByUnidad(
                UnidadExternoId.of(query.unidadExternoId()),
                TenantId.of(query.tenantId())
        );
        if (estadoOpt.isEmpty()) {
            return Result.failure(ErrorCode.RECURSO_NO_ENCONTRADO);
        }
        return Result.success(toDto(estadoOpt.get()));
    }

    private EstadoUnidadResponseDto toDto(EstadoUnidadView e) {
        return new EstadoUnidadResponseDto(
                e.unidadExternoId(),
                e.tenantId(),
                e.rutaExternoId(),
                e.latitud(),
                e.longitud(),
                e.ultimaVelocidadKmh(),
                e.ultimoPingEn(),
                e.estadoMovimiento(),
                e.actualizadoEn()
        );
    }
}
