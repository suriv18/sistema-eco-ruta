package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.RefreshTokenCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.LoginResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RefreshTokenUseCase {
    Result<LoginResponseDto> refresh(RefreshTokenCommand command);
}
