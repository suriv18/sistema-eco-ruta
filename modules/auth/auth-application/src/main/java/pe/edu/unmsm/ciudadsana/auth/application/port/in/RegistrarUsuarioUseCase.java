package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.RegistrarUsuarioCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarUsuarioUseCase {
    Result<UsuarioResponseDto> registrar(RegistrarUsuarioCommand command);
}
