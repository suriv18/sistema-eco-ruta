package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.RefreshTokenCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.LoginResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.JwtTokenPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RefreshTokenPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.enums.EstadoUsuario;
import pe.edu.unmsm.ciudadsana.auth.domain.model.RefreshToken;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Email;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.NombresCompletos;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenCommandHandlerTest {

    @Mock
    RefreshTokenPersistencePort refreshTokenPort;

    @Mock
    UsuarioPersistencePort usuarioPort;

    @Mock
    JwtTokenPort jwtTokenPort;

    @Mock
    EventPublisherPort eventPublisher;

    @InjectMocks
    RefreshTokenCommandHandler handler;

    private static final UUID USUARIO_ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private RefreshToken tokenVigente() {
        return RefreshToken.reconstitute(
                RefreshTokenId.of(UUID.randomUUID()),
                UsuarioId.of(USUARIO_ID),
                "hash",
                Instant.now().plus(7, ChronoUnit.DAYS),
                false,
                Instant.now(),
                null
        );
    }

    private RefreshToken tokenExpirado() {
        return RefreshToken.reconstitute(
                RefreshTokenId.of(UUID.randomUUID()),
                UsuarioId.of(USUARIO_ID),
                "hash-expirado",
                Instant.now().minus(1, ChronoUnit.DAYS),
                false,
                Instant.now().minus(8, ChronoUnit.DAYS),
                null
        );
    }

    private Usuario usuarioActivo() {
        return Usuario.reconstitute(
                UsuarioId.of(USUARIO_ID),
                TenantId.of(TENANT_ID),
                NombresCompletos.of("Ana", "Garcia"),
                Email.of("ana@test.com"),
                Username.of("ana"),
                PasswordHash.of("hash"),
                null,
                EstadoUsuario.ACTIVO,
                null,
                Instant.now(),
                null,
                Set.of()
        );
    }

    @Test
    void refresh_tokenVigente_retornaNewTokens() {
        when(refreshTokenPort.findByTokenHash("hash")).thenReturn(Optional.of(tokenVigente()));
        when(usuarioPort.findById(any())).thenReturn(Optional.of(usuarioActivo()));
        when(jwtTokenPort.generateAccessToken(any(), any(), anyString(), any())).thenReturn("new-access-token");
        when(jwtTokenPort.generateRefreshToken(any())).thenReturn("new-refresh-token");
        when(refreshTokenPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Result<LoginResponseDto> result = handler.refresh(new RefreshTokenCommand("hash"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().accessToken()).isEqualTo("new-access-token");
        assertThat(result.getValue().refreshToken()).isEqualTo("new-refresh-token");
    }

    @Test
    void refresh_tokenNoExiste_retornaInvalido() {
        when(refreshTokenPort.findByTokenHash(anyString())).thenReturn(Optional.empty());

        Result<LoginResponseDto> result = handler.refresh(new RefreshTokenCommand("token-inexistente"));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.REFRESH_TOKEN_INVALIDO);
    }

    @Test
    void refresh_tokenExpirado_retornaInvalido() {
        when(refreshTokenPort.findByTokenHash("hash-expirado")).thenReturn(Optional.of(tokenExpirado()));

        Result<LoginResponseDto> result = handler.refresh(new RefreshTokenCommand("hash-expirado"));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.REFRESH_TOKEN_INVALIDO);
    }

    @Test
    void refresh_usuarioNoExiste_retornaNoEncontrado() {
        when(refreshTokenPort.findByTokenHash("hash")).thenReturn(Optional.of(tokenVigente()));
        when(usuarioPort.findById(any())).thenReturn(Optional.empty());

        Result<LoginResponseDto> result = handler.refresh(new RefreshTokenCommand("hash"));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_NO_ENCONTRADO);
    }
}
