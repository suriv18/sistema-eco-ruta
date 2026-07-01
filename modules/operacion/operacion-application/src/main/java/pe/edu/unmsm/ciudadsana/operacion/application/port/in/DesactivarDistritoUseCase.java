package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarDistritoCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface DesactivarDistritoUseCase {
    Result<Void> desactivar(DesactivarDistritoCommand cmd);
}
