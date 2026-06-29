package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ObtenerRutaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerRutaUseCase {
    Result<RutaResponseDto> obtener(ObtenerRutaQuery query);
}
