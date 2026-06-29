package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarDispositivoCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DispositivoResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarDispositivoUseCase {
    Result<DispositivoResponseDto> registrar(RegistrarDispositivoCommand command);
}
