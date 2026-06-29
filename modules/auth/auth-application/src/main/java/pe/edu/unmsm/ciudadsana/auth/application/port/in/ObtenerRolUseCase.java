package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerRolQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerRolUseCase {
    Result<RolResponseDto> obtener(ObtenerRolQuery query);
}
