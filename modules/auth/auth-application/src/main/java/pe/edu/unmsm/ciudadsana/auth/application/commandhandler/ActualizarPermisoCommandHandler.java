package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.auth.application.command.ActualizarPermisoCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ActualizarPermisoUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ActualizarPermisoCommandHandler implements ActualizarPermisoUseCase {

    private final PermisoPersistencePort permisoPort;

    public ActualizarPermisoCommandHandler(PermisoPersistencePort permisoPort) {
        this.permisoPort = permisoPort;
    }

    @Transactional
    @Override
    public Result<PermisoResponseDto> actualizar(ActualizarPermisoCommand command) {
        return permisoPort.findById(PermisoId.of(command.permisoId()))
                .map(existing -> {
                    Permiso actualizado = Permiso.reconstitute(
                            existing.getId(),
                            existing.getCodigo(),
                            command.modulo(),
                            command.descripcion()
                    );
                    Permiso saved = permisoPort.save(actualizado);
                    return Result.success(PermisoResponseDto.from(saved));
                })
                .orElseGet(() -> Result.failure(ErrorCode.PERMISO_NO_ENCONTRADO));
    }
}
