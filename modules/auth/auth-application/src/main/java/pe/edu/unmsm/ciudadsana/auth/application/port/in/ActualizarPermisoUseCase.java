package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.ActualizarPermisoCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ActualizarPermisoUseCase {
    Result<PermisoResponseDto> actualizar(ActualizarPermisoCommand command);
}
