package pe.edu.unmsm.ciudadsana.auditoria.application.port.in;

import pe.edu.unmsm.ciudadsana.auditoria.application.command.RegistrarEventoAuditoriaCommand;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface RegistrarEventoAuditoriaUseCase {
    Result<Void> registrar(RegistrarEventoAuditoriaCommand command);
}
