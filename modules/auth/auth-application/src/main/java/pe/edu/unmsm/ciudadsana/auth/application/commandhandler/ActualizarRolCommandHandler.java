package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.auth.application.command.ActualizarRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ActualizarRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class ActualizarRolCommandHandler implements ActualizarRolUseCase {

    private final RolPersistencePort rolPort;

    public ActualizarRolCommandHandler(RolPersistencePort rolPort) {
        this.rolPort = rolPort;
    }

    @Transactional
    @Override
    public Result<RolResponseDto> actualizar(ActualizarRolCommand command) {
        Optional<Rol> rolOpt = rolPort.findById(RolId.of(command.rolId()));
        if (rolOpt.isEmpty()) {
            return Result.failure(ErrorCode.ROL_NO_ENCONTRADO);
        }
        Rol rol = rolOpt.get();
        rol.actualizar(command.nombre(), command.descripcion());
        Rol saved = rolPort.save(rol);
        return Result.success(RolResponseDto.from(saved));
    }
}
