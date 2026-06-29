package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.command.ProcesarPingGpsCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.PingGpsResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ProcesarPingGpsUseCase {
    Result<PingGpsResponseDto> procesar(ProcesarPingGpsCommand command);
}
