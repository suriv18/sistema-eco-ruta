package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EstadoUnidadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ObtenerEstadoUnidadQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerEstadoUnidadUseCase {
    Result<EstadoUnidadResponseDto> obtener(ObtenerEstadoUnidadQuery query);
}
