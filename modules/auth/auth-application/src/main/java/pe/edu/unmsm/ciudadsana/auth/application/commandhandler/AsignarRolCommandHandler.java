package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.command.AsignarRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.AsignarRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;

@Component
public class AsignarRolCommandHandler implements AsignarRolUseCase {

    private final UsuarioPersistencePort usuarioPort;
    private final RolPersistencePort rolPort;
    private final EventPublisherPort eventPublisher;

    public AsignarRolCommandHandler(
            UsuarioPersistencePort usuarioPort,
            RolPersistencePort rolPort,
            EventPublisherPort eventPublisher) {
        this.usuarioPort = usuarioPort;
        this.rolPort = rolPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Void> asignarRol(AsignarRolCommand command) {
        Optional<Usuario> usuarioOpt = usuarioPort.findById(UsuarioId.of(command.usuarioId()));
        if (usuarioOpt.isEmpty()) {
            return Result.failure(ErrorCode.USUARIO_NO_ENCONTRADO);
        }

        RolId rolId = RolId.of(command.rolId());
        Optional<Rol> rolOpt = rolPort.findById(rolId);
        if (rolOpt.isEmpty()) {
            return Result.failure(ErrorCode.ROL_NO_ENCONTRADO);
        }

        Usuario usuario = usuarioOpt.get();
        usuario.asignarRol(rolId, Instant.now());
        usuarioPort.save(usuario);
        eventPublisher.publishAll(usuario.pullDomainEvents());

        return Result.success(null);
    }
}
