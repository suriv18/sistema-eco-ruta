package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.auth.application.command.AsignarPermisoARolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.AsignarPermisoARolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class AsignarPermisoARolCommandHandler implements AsignarPermisoARolUseCase {

    private final RolPersistencePort rolPort;
    private final PermisoPersistencePort permisoPort;

    public AsignarPermisoARolCommandHandler(RolPersistencePort rolPort, PermisoPersistencePort permisoPort) {
        this.rolPort = rolPort;
        this.permisoPort = permisoPort;
    }

    @Override
    @Transactional
    public Result<Void> asignar(AsignarPermisoARolCommand command) {
        if (rolPort.findById(RolId.of(command.rolId())).isEmpty()) {
            return Result.failure(ErrorCode.ROL_NO_ENCONTRADO);
        }
        if (permisoPort.findById(PermisoId.of(command.permisoId())).isEmpty()) {
            return Result.failure(ErrorCode.PERMISO_NO_ENCONTRADO);
        }
        if (rolPort.tienePermiso(command.rolId(), command.permisoId())) {
            return Result.failure(ErrorCode.PERMISO_DUPLICADO);
        }
        rolPort.asignarPermiso(command.rolId(), command.permisoId());
        return Result.success(null);
    }
}
