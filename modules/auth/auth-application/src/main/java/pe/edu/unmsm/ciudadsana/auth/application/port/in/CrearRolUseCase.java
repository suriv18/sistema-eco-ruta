package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.CrearRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface CrearRolUseCase {
    Result<RolResponseDto> crear(CrearRolCommand command);
}
