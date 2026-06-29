package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.command.QuitarPermisoDeRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.QuitarPermisoDeRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class QuitarPermisoDeRolCommandHandler implements QuitarPermisoDeRolUseCase {

    private final RolPersistencePort rolPort;
    private final PermisoPersistencePort permisoPort;

    public QuitarPermisoDeRolCommandHandler(RolPersistencePort rolPort, PermisoPersistencePort permisoPort) {
        this.rolPort = rolPort;
        this.permisoPort = permisoPort;
    }

    @Override
    public Result<Void> quitar(QuitarPermisoDeRolCommand command) {
        if (rolPort.findById(RolId.of(command.rolId())).isEmpty()) {
            return Result.failure(ErrorCode.ROL_NO_ENCONTRADO);
        }
        if (permisoPort.findById(PermisoId.of(command.permisoId())).isEmpty()) {
            return Result.failure(ErrorCode.PERMISO_NO_ENCONTRADO);
        }
        rolPort.quitarPermiso(command.rolId(), command.permisoId());
        return Result.success(null);
    }
}
