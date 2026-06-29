package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.command.RegistrarUsuarioCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.RegistrarUsuarioUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PasswordEncoderPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Email;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.NombresCompletos;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Username;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RegistrarUsuarioCommandHandler implements RegistrarUsuarioUseCase {

    private final UsuarioPersistencePort usuarioPort;
    private final PasswordEncoderPort passwordEncoder;
    private final EventPublisherPort eventPublisher;

    public RegistrarUsuarioCommandHandler(
            UsuarioPersistencePort usuarioPort,
            PasswordEncoderPort passwordEncoder,
            EventPublisherPort eventPublisher) {
        this.usuarioPort = usuarioPort;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<UsuarioResponseDto> registrar(RegistrarUsuarioCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        Email email = Email.of(command.email());
        Username username = Username.of(command.username());

        if (usuarioPort.existsByEmail(email, tenantId)) {
            return Result.failure(ErrorCode.USUARIO_YA_EXISTE);
        }

        if (usuarioPort.existsByUsername(username, tenantId)) {
            return Result.failure(ErrorCode.USUARIO_YA_EXISTE, "Ya existe un usuario con ese username");
        }

        PasswordHash hash = passwordEncoder.encode(command.password());
        UsuarioId id = UsuarioId.of(UUID.randomUUID());
        NombresCompletos nombres = NombresCompletos.of(command.nombres(), command.apellidos());

        Usuario usuario = Usuario.create(id, tenantId, nombres, email, username, hash, command.telefono(), Instant.now());
        usuarioPort.save(usuario);
        eventPublisher.publishAll(usuario.pullDomainEvents());

        return Result.success(toDto(usuario));
    }

    private UsuarioResponseDto toDto(Usuario usuario) {
        Set<String> roles = usuario.getRoles().stream()
                .map(rolId -> rolId.value().toString())
                .collect(Collectors.toSet());

        return new UsuarioResponseDto(
                usuario.getId().value(),
                usuario.getTenantId().value(),
                usuario.getNombresCompletos().nombres(),
                usuario.getNombresCompletos().apellidos(),
                usuario.getEmail().value(),
                usuario.getUsername().value(),
                usuario.getTelefono(),
                usuario.getEstado().name(),
                roles,
                usuario.getCreadoEn());
    }
}
