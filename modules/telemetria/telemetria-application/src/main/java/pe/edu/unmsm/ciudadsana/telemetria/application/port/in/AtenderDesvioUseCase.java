package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.command.AtenderDesvioCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface AtenderDesvioUseCase {
    Result<Void> atender(AtenderDesvioCommand command);
}
