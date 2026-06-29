package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.CrearPermisoCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface CrearPermisoUseCase {
    Result<PermisoResponseDto> crear(CrearPermisoCommand command);
}
