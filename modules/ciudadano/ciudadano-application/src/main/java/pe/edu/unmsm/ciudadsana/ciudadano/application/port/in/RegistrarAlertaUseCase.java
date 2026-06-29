package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.command.RegistrarAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarAlertaUseCase {
    Result<AlertaResponseDto> registrar(RegistrarAlertaCommand command);
}
