package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.command.CancelarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface CancelarRutaUseCase {
    Result<RutaResponseDto> cancelar(CancelarRutaCommand command);
}
