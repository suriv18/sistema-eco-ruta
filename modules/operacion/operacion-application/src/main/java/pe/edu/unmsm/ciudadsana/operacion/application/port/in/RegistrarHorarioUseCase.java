package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarHorarioCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarHorarioUseCase {
    Result<HorarioResponseDto> registrar(RegistrarHorarioCommand command);
}
