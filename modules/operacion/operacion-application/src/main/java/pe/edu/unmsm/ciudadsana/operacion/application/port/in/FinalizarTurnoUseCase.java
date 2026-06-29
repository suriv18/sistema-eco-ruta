package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.FinalizarTurnoCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface FinalizarTurnoUseCase {
    Result<Void> finalizar(FinalizarTurnoCommand command);
}
