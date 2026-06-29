package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.command.IniciarTurnoCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface IniciarTurnoUseCase {
    Result<Void> iniciar(IniciarTurnoCommand command);
}
