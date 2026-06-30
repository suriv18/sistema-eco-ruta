package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.LogoutCommand;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RefreshTokenPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.RefreshToken;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RefreshTokenId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogoutCommandHandlerTest {

    @Mock
    RefreshTokenPersistencePort refreshTokenPort;

    @InjectMocks
    LogoutCommandHandler handler;

    private static final UUID USUARIO_ID = UUID.randomUUID();

    @Test
    void logout_tokenExistente_revocaTodosYRetornaSuccess() {
        RefreshToken token = RefreshToken.create(
                RefreshTokenId.of(UUID.randomUUID()),
                UsuarioId.of(USUARIO_ID),
                "token-hash",
                Instant.now().plus(7, ChronoUnit.DAYS),
                Instant.now());
        when(refreshTokenPort.findByTokenHash("token-hash")).thenReturn(Optional.of(token));

        Result<Void> result = handler.logout(new LogoutCommand(USUARIO_ID, "token-hash"));

        assertThat(result.isSuccess()).isTrue();
        verify(refreshTokenPort).revokeAllByUsuarioId(any());
    }

    @Test
    void logout_tokenNoExiste_igualRetornaSuccess() {
        when(refreshTokenPort.findByTokenHash(anyString())).thenReturn(Optional.empty());

        Result<Void> result = handler.logout(new LogoutCommand(USUARIO_ID, "token-inexistente"));

        assertThat(result.isSuccess()).isTrue();
        verify(refreshTokenPort, never()).revokeAllByUsuarioId(any());
    }
}
