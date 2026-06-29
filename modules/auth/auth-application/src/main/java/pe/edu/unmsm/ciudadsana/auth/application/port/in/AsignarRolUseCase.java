package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.AsignarRolCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface AsignarRolUseCase {
    Result<Void> asignarRol(AsignarRolCommand command);
}
