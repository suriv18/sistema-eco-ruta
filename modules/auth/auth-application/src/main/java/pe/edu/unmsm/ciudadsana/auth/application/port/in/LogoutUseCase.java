package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.auth.application.command.LogoutCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface LogoutUseCase {
    Result<Void> logout(LogoutCommand command);
}
