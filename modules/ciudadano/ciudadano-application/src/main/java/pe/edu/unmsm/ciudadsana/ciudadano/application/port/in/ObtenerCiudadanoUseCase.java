package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ObtenerCiudadanoQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerCiudadanoUseCase {
    Result<CiudadanoResponseDto> obtener(ObtenerCiudadanoQuery query);
}
