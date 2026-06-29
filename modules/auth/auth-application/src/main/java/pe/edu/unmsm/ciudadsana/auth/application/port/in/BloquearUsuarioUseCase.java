package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.BloquearUsuarioCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface BloquearUsuarioUseCase {
    Result<Void> bloquear(BloquearUsuarioCommand command);
}
