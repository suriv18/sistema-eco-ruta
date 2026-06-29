package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CrearTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface CrearTurnoUseCase {
    Result<TurnoResponseDto> crear(CrearTurnoCommand command);
}
