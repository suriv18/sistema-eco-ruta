package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarChoferCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface RegistrarChoferUseCase {
    Result<ChoferResponseDto> registrar(RegistrarChoferCommand command);
}
