package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.command.ActivarDistritoCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ActivarDistritoUseCase {
    Result<Void> activar(ActivarDistritoCommand cmd);
}
