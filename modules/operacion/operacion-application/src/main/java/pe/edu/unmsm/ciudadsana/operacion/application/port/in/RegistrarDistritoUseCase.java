package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarDistritoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface RegistrarDistritoUseCase {
    Result<DistritoResponseDto> registrar(RegistrarDistritoCommand command);
}
