package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.command.IniciarEjecucionRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface IniciarEjecucionRutaUseCase {
    Result<RutaResponseDto> iniciarEjecucion(IniciarEjecucionRutaCommand command);
}
