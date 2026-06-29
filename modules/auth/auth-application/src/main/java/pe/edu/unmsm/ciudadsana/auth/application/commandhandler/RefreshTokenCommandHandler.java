package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.command.RefreshTokenCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.LoginResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.RefreshTokenUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.JwtTokenPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RefreshTokenPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.RefreshToken;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RefreshTokenId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RefreshTokenCommandHandler implements RefreshTokenUseCase {

    private final RefreshTokenPersistencePort refreshTokenPort;
    private final UsuarioPersistencePort usuarioPort;
    private final JwtTokenPort jwtTokenPort;
    private final EventPublisherPort eventPublisher;

    public RefreshTokenCommandHandler(
            RefreshTokenPersistencePort refreshTokenPort,
            UsuarioPersistencePort usuarioPort,
            JwtTokenPort jwtTokenPort,
            EventPublisherPort eventPublisher) {
        this.refreshTokenPort = refreshTokenPort;
        this.usuarioPort = usuarioPort;
        this.jwtTokenPort = jwtTokenPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<LoginResponseDto> refresh(RefreshTokenCommand command) {
        Optional<RefreshToken> tokenOpt = refreshTokenPort.findByTokenHash(command.refreshToken());
        if (tokenOpt.isEmpty()) {
            return Result.failure(ErrorCode.REFRESH_TOKEN_INVALIDO);
        }

        RefreshToken token = tokenOpt.get();
        if (!token.estaVigente(Instant.now())) {
            return Result.failure(ErrorCode.REFRESH_TOKEN_INVALIDO);
        }

        UsuarioId usuarioId = token.getUsuarioId();
        Optional<Usuario> usuarioOpt = usuarioPort.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return Result.failure(ErrorCode.USUARIO_NO_ENCONTRADO);
        }

        Usuario usuario = usuarioOpt.get();
        refreshTokenPort.revokeAllByUsuarioId(usuarioId);

        Set<String> roles = usuario.getRoles().stream()
                .map(rolId -> rolId.value().toString())
                .collect(Collectors.toSet());

        String newAccessToken = jwtTokenPort.generateAccessToken(
                usuarioId, usuario.getTenantId(), usuario.getUsername().value(), roles);
        String newRefreshTokenString = jwtTokenPort.generateRefreshToken(usuarioId);

        Instant now = Instant.now();
        RefreshToken newRefreshToken = RefreshToken.create(
                RefreshTokenId.of(UUID.randomUUID()),
                usuarioId,
                newRefreshTokenString,
                now.plus(7, ChronoUnit.DAYS),
                now);
        refreshTokenPort.save(newRefreshToken);

        LoginResponseDto response = new LoginResponseDto(
                newAccessToken,
                newRefreshTokenString,
                "Bearer",
                3600L,
                usuarioId.value(),
                usuario.getUsername().value(),
                roles);

        return Result.success(response);
    }
}
