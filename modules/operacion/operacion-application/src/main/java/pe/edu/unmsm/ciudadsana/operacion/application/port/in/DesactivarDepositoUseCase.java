package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarDepositoCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface DesactivarDepositoUseCase {
    Result<Void> desactivar(DesactivarDepositoCommand cmd);
}
