package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.ActivarUsuarioCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ActivarUsuarioUseCase {
    Result<Void> activar(ActivarUsuarioCommand command);
}
