package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarHorariosPorZonaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarHorariosPorZonaUseCase {
    Result<PageResult<HorarioResponseDto>> listar(ListarHorariosPorZonaQuery query);
}
