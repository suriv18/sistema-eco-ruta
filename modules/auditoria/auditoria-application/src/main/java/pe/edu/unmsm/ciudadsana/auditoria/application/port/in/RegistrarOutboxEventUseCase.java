package pe.edu.unmsm.ciudadsana.auditoria.application.port.in;

import pe.edu.unmsm.ciudadsana.auditoria.application.command.RegistrarOutboxEventCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarOutboxEventUseCase {
    Result<Void> registrar(RegistrarOutboxEventCommand command);
}
