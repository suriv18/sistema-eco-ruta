package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.command.AgregarFotoAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface AgregarFotoAlertaUseCase {
    Result<AlertaResponseDto> agregarFoto(AgregarFotoAlertaCommand command);
}
