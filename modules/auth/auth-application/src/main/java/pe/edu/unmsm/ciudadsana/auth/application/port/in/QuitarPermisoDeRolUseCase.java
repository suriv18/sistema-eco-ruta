package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.QuitarPermisoDeRolCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface QuitarPermisoDeRolUseCase {
    Result<Void> quitar(QuitarPermisoDeRolCommand command);
}
