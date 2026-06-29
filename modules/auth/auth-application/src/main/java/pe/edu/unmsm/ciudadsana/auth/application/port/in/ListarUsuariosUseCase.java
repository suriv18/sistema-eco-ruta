package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarUsuariosQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

public interface ListarUsuariosUseCase {
    Result<PageResult<UsuarioResponseDto>> listar(ListarUsuariosQuery query);
}
