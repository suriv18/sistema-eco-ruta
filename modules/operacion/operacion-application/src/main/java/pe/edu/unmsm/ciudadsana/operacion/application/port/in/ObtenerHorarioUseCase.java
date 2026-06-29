package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerHorarioQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerHorarioUseCase {
    Result<HorarioResponseDto> obtener(ObtenerHorarioQuery query);
}
