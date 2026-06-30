package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarHorarioCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface DesactivarHorarioUseCase {
    Result<Void> desactivar(DesactivarHorarioCommand command);
}
