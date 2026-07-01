package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerContenedorQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerContenedorUseCase {
    Result<ContenedorResponseDto> obtener(ObtenerContenedorQuery query);
}
