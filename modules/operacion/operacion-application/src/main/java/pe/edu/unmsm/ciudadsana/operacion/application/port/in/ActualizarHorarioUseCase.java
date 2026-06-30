package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.command.ActualizarHorarioCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ActualizarHorarioUseCase {
    Result<HorarioResponseDto> actualizar(ActualizarHorarioCommand command);
}
