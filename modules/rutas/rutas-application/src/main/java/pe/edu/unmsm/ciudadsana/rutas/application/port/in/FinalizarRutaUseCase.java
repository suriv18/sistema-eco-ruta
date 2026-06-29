package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.command.FinalizarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface FinalizarRutaUseCase {
    Result<RutaResponseDto> finalizar(FinalizarRutaCommand command);
}
