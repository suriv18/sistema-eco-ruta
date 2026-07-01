package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoChoferCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface CambiarEstadoChoferUseCase {
    Result<ChoferResponseDto> cambiarEstado(CambiarEstadoChoferCommand cmd);
}
