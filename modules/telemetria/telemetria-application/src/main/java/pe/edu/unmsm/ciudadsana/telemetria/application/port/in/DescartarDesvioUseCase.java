package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.command.DescartarDesvioCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface DescartarDesvioUseCase {
    Result<Void> descartar(DescartarDesvioCommand command);
}
