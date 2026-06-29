package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.DesactivarRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;
import java.util.UUID;

@Component
public class DesactivarRolCommandHandler implements DesactivarRolUseCase {

    private final RolPersistencePort rolPort;

    public DesactivarRolCommandHandler(RolPersistencePort rolPort) {
        this.rolPort = rolPort;
    }

    @Override
    public Result<Void> desactivar(UUID rolId) {
        Optional<Rol> rolOpt = rolPort.findById(RolId.of(rolId));
        if (rolOpt.isEmpty()) {
            return Result.failure(ErrorCode.ROL_NO_ENCONTRADO);
        }
        Rol rol = rolOpt.get();
        rol.desactivar();
        rolPort.save(rol);
        return Result.success(null);
    }
}
