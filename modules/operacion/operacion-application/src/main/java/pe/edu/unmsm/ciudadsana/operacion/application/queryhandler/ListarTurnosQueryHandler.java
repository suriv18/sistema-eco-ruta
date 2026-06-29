package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarTurnosUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarTurnosQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarTurnosQueryHandler implements ListarTurnosUseCase {

    private final TurnosPersistencePort turnosPersistencePort;

    public ListarTurnosQueryHandler(TurnosPersistencePort turnosPersistencePort) {
        this.turnosPersistencePort = turnosPersistencePort;
    }

    @Override
    public Result<PageResult<TurnoResponseDto>> listar(ListarTurnosQuery query) {
        PageResult<Turno> pageResult = turnosPersistencePort.findAll(TenantId.of(query.tenantId()), query.page(), query.size());
        PageResult<TurnoResponseDto> mappedPage = pageResult.map(t -> new TurnoResponseDto(
            t.getId().value(),
            t.getTenantId().value(),
            t.getUnidadId().value(),
            t.getChoferId().value(),
            t.getDistritoId().value(),
            t.getFecha(),
            t.getHoraInicio(),
            t.getHoraFin(),
            t.getTipo().name(),
            t.getEstado().name(),
            t.getCreadoEn()
        ));
        return Result.success(mappedPage);
    }
}
