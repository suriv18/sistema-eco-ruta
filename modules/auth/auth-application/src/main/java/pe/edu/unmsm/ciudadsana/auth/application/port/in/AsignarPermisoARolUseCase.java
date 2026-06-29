package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.AsignarPermisoARolCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface AsignarPermisoARolUseCase {
    Result<Void> asignar(AsignarPermisoARolCommand command);
}
