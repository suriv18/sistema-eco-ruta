package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarContenedorCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface RegistrarContenedorUseCase {
    Result<ContenedorResponseDto> registrar(RegistrarContenedorCommand command);
}
