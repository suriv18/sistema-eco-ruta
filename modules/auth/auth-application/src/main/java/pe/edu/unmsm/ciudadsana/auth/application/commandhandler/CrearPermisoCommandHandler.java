package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.auth.application.command.CrearPermisoCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.CrearPermisoUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

@Component
public class CrearPermisoCommandHandler implements CrearPermisoUseCase {

    private final PermisoPersistencePort permisoPort;

    public CrearPermisoCommandHandler(PermisoPersistencePort permisoPort) {
        this.permisoPort = permisoPort;
    }

    @Transactional
    @Override
    public Result<PermisoResponseDto> crear(CrearPermisoCommand command) {
        if (permisoPort.existsByCodigo(command.codigo())) {
            return Result.failure(ErrorCode.PERMISO_DUPLICADO);
        }
        Permiso permiso = Permiso.create(PermisoId.of(UUID.randomUUID()), command.codigo(), command.modulo(), command.descripcion());
        Permiso saved = permisoPort.save(permiso);
        return Result.success(PermisoResponseDto.from(saved));
    }
}
