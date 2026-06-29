package pe.edu.unmsm.ciudadsana.auth.application.port.in;

import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

public interface DesactivarRolUseCase {
    Result<Void> desactivar(UUID rolId);
}
