package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoUnidadCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface CambiarEstadoUnidadUseCase {
    Result<Void> cambiarEstado(CambiarEstadoUnidadCommand command);
}
