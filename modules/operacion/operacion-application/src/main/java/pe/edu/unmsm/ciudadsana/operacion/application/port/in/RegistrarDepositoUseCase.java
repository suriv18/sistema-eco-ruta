package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarDepositoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface RegistrarDepositoUseCase {
    Result<DepositoResponseDto> registrar(RegistrarDepositoCommand command);
}
