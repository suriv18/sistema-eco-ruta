package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerCurrentUserQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerCurrentUserUseCase {
    Result<UsuarioResponseDto> obtenerCurrentUser(ObtenerCurrentUserQuery query);
}
