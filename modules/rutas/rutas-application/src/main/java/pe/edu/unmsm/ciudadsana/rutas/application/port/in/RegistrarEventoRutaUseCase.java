package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.command.RegistrarEventoRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarEventoRutaUseCase {
    Result<RutaResponseDto> registrar(RegistrarEventoRutaCommand command);
}
