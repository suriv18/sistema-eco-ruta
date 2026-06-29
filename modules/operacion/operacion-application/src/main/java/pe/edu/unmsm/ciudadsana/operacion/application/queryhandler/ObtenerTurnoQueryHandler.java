package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerTurnoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerTurnoQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class ObtenerTurnoQueryHandler implements ObtenerTurnoUseCase {

    private final TurnosPersistencePort turnosPersistencePort;

    public ObtenerTurnoQueryHandler(TurnosPersistencePort turnosPersistencePort) {
        this.turnosPersistencePort = turnosPersistencePort;
    }

    @Override
    public Result<TurnoResponseDto> obtener(ObtenerTurnoQuery query) {
        Optional<Turno> turnoOpt = turnosPersistencePort.findById(TurnoId.of(query.turnoId()), TenantId.of(query.tenantId()));
        if (turnoOpt.isEmpty()) return Result.failure(ErrorCode.TURNO_NO_ENCONTRADO);
        Turno turno = turnoOpt.get();
        return Result.success(new TurnoResponseDto(
            turno.getId().value(),
            turno.getTenantId().value(),
            turno.getUnidadId().value(),
            turno.getChoferId().value(),
            turno.getDistritoId().value(),
            turno.getFecha(),
            turno.getHoraInicio(),
            turno.getHoraFin(),
            turno.getTipo().name(),
            turno.getEstado().name(),
            turno.getCreadoEn()
        ));
    }
}
