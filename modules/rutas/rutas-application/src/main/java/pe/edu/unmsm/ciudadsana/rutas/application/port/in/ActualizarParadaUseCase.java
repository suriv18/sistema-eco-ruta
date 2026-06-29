package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.command.ActualizarParadaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ActualizarParadaUseCase {
    Result<RutaResponseDto> actualizar(ActualizarParadaCommand command);
}
