package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarHorariosPorZonaUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarHorariosPorZonaQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarHorariosPorZonaQueryHandler implements ListarHorariosPorZonaUseCase {

    private final HorariosPersistencePort horariosPersistencePort;

    public ListarHorariosPorZonaQueryHandler(HorariosPersistencePort horariosPersistencePort) {
        this.horariosPersistencePort = horariosPersistencePort;
    }

    @Override
    public Result<PageResult<HorarioResponseDto>> listar(ListarHorariosPorZonaQuery query) {
        PageResult<HorarioRecoleccion> pageResult = horariosPersistencePort.findAllByZona(
            ZonaId.of(query.zonaId()),
            TenantId.of(query.tenantId()),
            query.page(),
            query.size()
        );
        PageResult<HorarioResponseDto> mappedPage = pageResult.map(h -> new HorarioResponseDto(
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
        return Result.success(mappedPage);
    }
}
