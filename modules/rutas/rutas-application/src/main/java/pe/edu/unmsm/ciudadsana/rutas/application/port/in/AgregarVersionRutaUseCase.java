package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.command.AgregarVersionRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface AgregarVersionRutaUseCase {
    Result<RutaResponseDto> agregarVersion(AgregarVersionRutaCommand command);
}
