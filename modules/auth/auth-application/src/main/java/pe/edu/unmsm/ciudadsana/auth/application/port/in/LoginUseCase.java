package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.LoginCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.LoginResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface LoginUseCase {
    Result<LoginResponseDto> login(LoginCommand command);
}
