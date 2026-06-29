package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerPermisoQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerPermisoUseCase {
    Result<PermisoResponseDto> obtener(ObtenerPermisoQuery query);
}
