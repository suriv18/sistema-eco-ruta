package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.command.CambiarEstadoAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface CambiarEstadoAlertaUseCase {
    Result<AlertaResponseDto> cambiarEstado(CambiarEstadoAlertaCommand command);
}
