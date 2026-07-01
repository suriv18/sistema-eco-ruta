package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.command.CancelarTurnoCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface CancelarTurnoUseCase {
    Result<Void> cancelar(CancelarTurnoCommand cmd);
}
