package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.command.RegistrarCiudadanoCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarCiudadanoUseCase {
    Result<CiudadanoResponseDto> registrar(RegistrarCiudadanoCommand command);
}
