package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerChoferQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerChoferUseCase {
    Result<ChoferResponseDto> obtener(ObtenerChoferQuery query);
}
