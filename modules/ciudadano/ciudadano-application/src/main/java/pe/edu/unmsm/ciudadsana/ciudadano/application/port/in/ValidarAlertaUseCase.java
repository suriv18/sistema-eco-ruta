package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.command.ValidarAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ValidarAlertaUseCase {
    Result<AlertaResponseDto> validar(ValidarAlertaCommand command);
}
