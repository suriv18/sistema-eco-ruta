package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.ActualizarRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ActualizarRolUseCase {
    Result<RolResponseDto> actualizar(ActualizarRolCommand command);
}
