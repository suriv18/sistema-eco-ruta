package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarUnidadCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface RegistrarUnidadUseCase {
    Result<UnidadResponseDto> registrar(RegistrarUnidadCommand command);
}
