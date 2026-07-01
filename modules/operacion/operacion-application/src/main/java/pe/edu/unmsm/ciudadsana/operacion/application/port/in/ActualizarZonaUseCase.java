package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.command.ActualizarZonaCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ActualizarZonaUseCase {
    Result<ZonaResponseDto> actualizar(ActualizarZonaCommand cmd);
}
