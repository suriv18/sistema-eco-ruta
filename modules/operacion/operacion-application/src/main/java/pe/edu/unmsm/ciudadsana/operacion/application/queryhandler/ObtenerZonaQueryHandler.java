package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerZonaUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerZonaQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class ObtenerZonaQueryHandler implements ObtenerZonaUseCase {

    private final ZonasPersistencePort zonasPersistencePort;

    public ObtenerZonaQueryHandler(ZonasPersistencePort zonasPersistencePort) {
        this.zonasPersistencePort = zonasPersistencePort;
    }

    @Override
    public Result<ZonaResponseDto> obtener(ObtenerZonaQuery query) {
        Optional<Zona> zonaOpt = zonasPersistencePort.findById(ZonaId.of(query.zonaId()), TenantId.of(query.tenantId()));
        if (zonaOpt.isEmpty()) return Result.failure(ErrorCode.ZONA_NO_ENCONTRADA);
        Zona zona = zonaOpt.get();
        return Result.success(new ZonaResponseDto(
            zona.getId().value(),
            zona.getTenantId().value(),
            zona.getDistritoId().value(),
            zona.getCodigo().value(),
            zona.getNombre(),
            zona.getTipo().name(),
            zona.getPrioridad().value(),
            zona.getEstado().name(),
            zona.getCreadoEn()
        ));
    }
}
