package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoContenedorCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface CambiarEstadoContenedorUseCase {
    Result<Void> cambiarEstado(CambiarEstadoContenedorCommand command);
}
