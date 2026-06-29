package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerHorarioUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerHorarioQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class ObtenerHorarioQueryHandler implements ObtenerHorarioUseCase {

    private final HorariosPersistencePort horariosPersistencePort;

    public ObtenerHorarioQueryHandler(HorariosPersistencePort horariosPersistencePort) {
        this.horariosPersistencePort = horariosPersistencePort;
    }

    @Override
    public Result<HorarioResponseDto> obtener(ObtenerHorarioQuery query) {
        Optional<HorarioRecoleccion> opt = horariosPersistencePort.findById(
            HorarioRecoleccionId.of(query.horarioId()),
            TenantId.of(query.tenantId())
        );
        if (opt.isEmpty()) return Result.failure(ErrorCode.HORARIO_NO_ENCONTRADO);
        HorarioRecoleccion h = opt.get();
        return Result.success(new HorarioResponseDto(
            h.getId().value(),
            h.getTenantId().value(),
            h.getZonaId().value(),
            h.getDiaSemana(),
            h.getHoraInicio(),
            h.getHoraFin(),
            h.getObservacion(),
            h.getEstado().name(),
            h.getCreadoEn()
        ));
    }
}
