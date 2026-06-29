package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.command.LoginCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.LoginResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.LoginUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.JwtTokenPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.LoginAuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PasswordEncoderPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RefreshTokenPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.enums.EstadoUsuario;
import pe.edu.unmsm.ciudadsana.auth.domain.model.RefreshToken;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RefreshTokenId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Username;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class LoginCommandHandler implements LoginUseCase {

    private final UsuarioPersistencePort usuarioPort;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;
    private final RefreshTokenPersistencePort refreshTokenPort;
    private final LoginAuditoriaPersistencePort auditoriaPort;
    private final EventPublisherPort eventPublisher;

    public LoginCommandHandler(
            UsuarioPersistencePort usuarioPort,
            PasswordEncoderPort passwordEncoder,
            JwtTokenPort jwtTokenPort,
            RefreshTokenPersistencePort refreshTokenPort,
            LoginAuditoriaPersistencePort auditoriaPort,
            EventPublisherPort eventPublisher) {
        this.usuarioPort = usuarioPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
        this.refreshTokenPort = refreshTokenPort;
        this.auditoriaPort = auditoriaPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<LoginResponseDto> login(LoginCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        Username username = Username.of(command.username());

        Optional<Usuario> usuarioOpt = usuarioPort.findByUsername(username, tenantId);
        if (usuarioOpt.isEmpty()) {
            auditoriaPort.registrar(null, command.username(), command.ipOrigen(), command.userAgent(), false, "Usuario no encontrado");
            return Result.failure(ErrorCode.CREDENCIALES_INVALIDAS);
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(command.password(), usuario.getPasswordHash())) {
            auditoriaPort.registrar(usuario.getId().value(), command.username(), command.ipOrigen(), command.userAgent(), false, "Contraseña incorrecta");
            return Result.failure(ErrorCode.CREDENCIALES_INVALIDAS);
        }

        if (usuario.getEstado() == EstadoUsuario.BLOQUEADO) {
            auditoriaPort.registrar(usuario.getId().value(), command.username(), command.ipOrigen(), command.userAgent(), false, "Usuario bloqueado");
            return Result.failure(ErrorCode.USUARIO_BLOQUEADO);
        }

        if (usuario.getEstado() == EstadoUsuario.INACTIVO) {
            auditoriaPort.registrar(usuario.getId().value(), command.username(), command.ipOrigen(), command.userAgent(), false, "Usuario inactivo");
            return Result.failure(ErrorCode.USUARIO_INACTIVO);
        }

        usuario.registrarLoginExitoso(Instant.now(), command.ipOrigen());
        usuarioPort.save(usuario);

        Set<String> roles = usuario.getRoles().stream()
                .map(rolId -> rolId.value().toString())
                .collect(Collectors.toSet());

        String accessToken = jwtTokenPort.generateAccessToken(
                usuario.getId(), tenantId, usuario.getUsername().value(), roles);
        String refreshTokenString = jwtTokenPort.generateRefreshToken(usuario.getId());

        RefreshToken refreshToken = RefreshToken.create(
                RefreshTokenId.of(UUID.randomUUID()),
                usuario.getId(),
                refreshTokenString,
                Instant.now().plus(7, ChronoUnit.DAYS),
                Instant.now());
        refreshTokenPort.save(refreshToken);

        eventPublisher.publishAll(usuario.pullDomainEvents());

        auditoriaPort.registrar(usuario.getId().value(), command.username(), command.ipOrigen(), command.userAgent(), true, null);

        LoginResponseDto response = new LoginResponseDto(
                accessToken,
                refreshTokenString,
                "Bearer",
                3600L,
                usuario.getId().value(),
                usuario.getUsername().value(),
                roles);

        return Result.success(response);
    }
}
