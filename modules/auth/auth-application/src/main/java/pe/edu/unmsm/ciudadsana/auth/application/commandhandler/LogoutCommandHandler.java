package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.command.LogoutCommand;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.LogoutUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RefreshTokenPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.RefreshToken;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class LogoutCommandHandler implements LogoutUseCase {

    private final RefreshTokenPersistencePort refreshTokenPort;

    public LogoutCommandHandler(RefreshTokenPersistencePort refreshTokenPort) {
        this.refreshTokenPort = refreshTokenPort;
    }

    @Override
    public Result<Void> logout(LogoutCommand command) {
        Optional<RefreshToken> tokenOpt = refreshTokenPort.findByTokenHash(command.refreshToken());
        if (tokenOpt.isPresent()) {
            refreshTokenPort.revokeAllByUsuarioId(UsuarioId.of(command.usuarioId()));
        }
        return Result.success(null);
    }
}
