package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.command.BloquearUsuarioCommand;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.BloquearUsuarioUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;

@Component
public class BloquearUsuarioCommandHandler implements BloquearUsuarioUseCase {

    private final UsuarioPersistencePort usuarioPort;
    private final EventPublisherPort eventPublisher;

    public BloquearUsuarioCommandHandler(UsuarioPersistencePort usuarioPort, EventPublisherPort eventPublisher) {
        this.usuarioPort = usuarioPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<Void> bloquear(BloquearUsuarioCommand command) {
        Optional<Usuario> usuarioOpt = usuarioPort.findById(UsuarioId.of(command.usuarioId()));
        if (usuarioOpt.isEmpty()) {
            return Result.failure(ErrorCode.USUARIO_NO_ENCONTRADO);
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getTenantId().value().equals(command.tenantId())) {
            return Result.failure(ErrorCode.USUARIO_SIN_PERMISO);
        }

        try {
            usuario.bloquear(Instant.now());
        } catch (IllegalStateException ex) {
            return Result.failure(ErrorCode.OPERACION_NO_PERMITIDA, ex.getMessage());
        }

        usuarioPort.save(usuario);
        eventPublisher.publishAll(usuario.pullDomainEvents());

        return Result.success(null);
    }
}
