package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarDesvioRutaCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DesvioRutaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarDesvioRutaUseCase {
    Result<DesvioRutaResponseDto> registrar(RegistrarDesvioRutaCommand command);
}
