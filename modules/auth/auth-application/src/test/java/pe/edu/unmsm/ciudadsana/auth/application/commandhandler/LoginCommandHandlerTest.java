package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.LoginCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.LoginResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.JwtTokenPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.LoginAuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PasswordEncoderPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RefreshTokenPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.enums.EstadoUsuario;
import pe.edu.unmsm.ciudadsana.auth.domain.model.RefreshToken;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Email;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.NombresCompletos;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RefreshTokenId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Username;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginCommandHandlerTest {

    @Mock
    UsuarioPersistencePort usuarioPort;

    @Mock
    RolPersistencePort rolPort;

    @Mock
    PasswordEncoderPort passwordEncoder;

    @Mock
    JwtTokenPort jwtTokenPort;

    @Mock
    RefreshTokenPersistencePort refreshTokenPort;

    @Mock
    LoginAuditoriaPersistencePort auditoriaPort;

    @Mock
    EventPublisherPort eventPublisher;

    @InjectMocks
    LoginCommandHandler handler;

    private static final UUID USUARIO_ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private Usuario usuarioActivo() {
        return Usuario.reconstitute(
                UsuarioId.of(USUARIO_ID),
                TenantId.of(TENANT_ID),
                NombresCompletos.of("Ana", "Garcia"),
                Email.of("ana@test.com"),
                Username.of("ana"),
                PasswordHash.of("hashed_password"),
                null,
                EstadoUsuario.ACTIVO,
                null,
                Instant.now(),
                null,
                Set.of()
        );
    }

    private Usuario usuarioBloqueado() {
        return Usuario.reconstitute(
                UsuarioId.of(USUARIO_ID),
                TenantId.of(TENANT_ID),
                NombresCompletos.of("Ana", "Garcia"),
                Email.of("ana@test.com"),
                Username.of("ana"),
                PasswordHash.of("hashed_password"),
                null,
                EstadoUsuario.BLOQUEADO,
                null,
                Instant.now(),
                null,
                Set.of()
        );
    }

    private Usuario usuarioInactivo() {
        return Usuario.reconstitute(
                UsuarioId.of(USUARIO_ID),
                TenantId.of(TENANT_ID),
                NombresCompletos.of("Ana", "Garcia"),
                Email.of("ana@test.com"),
                Username.of("ana"),
                PasswordHash.of("hashed_password"),
                null,
                EstadoUsuario.INACTIVO,
                null,
                Instant.now(),
                null,
                Set.of()
        );
    }

    private LoginCommand loginCommand() {
        return new LoginCommand("ana", "password123", TENANT_ID, "127.0.0.1", "Mozilla/5.0");
    }

    @Test
    void login_credencialesValidas_retornaTokens() {
        Rol rol = Rol.reconstitute(RolId.of(UUID.randomUUID()), "ADMIN", "Admin", "desc", true);
        when(usuarioPort.findByUsername(any(), any())).thenReturn(Optional.of(usuarioActivo()));
        when(passwordEncoder.matches(anyString(), any())).thenReturn(true);
        when(rolPort.findByIds(any())).thenReturn(List.of(rol));
        when(jwtTokenPort.generateAccessToken(any(), any(), anyString(), any())).thenReturn("access-token");
        when(jwtTokenPort.generateRefreshToken(any())).thenReturn("refresh-token");
        when(refreshTokenPort.save(any())).thenReturn(
                RefreshToken.create(
                        RefreshTokenId.of(UUID.randomUUID()),
                        UsuarioId.of(USUARIO_ID),
                        "refresh-token",
                        Instant.now().plus(7, ChronoUnit.DAYS),
                        Instant.now()));
        when(usuarioPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Result<LoginResponseDto> result = handler.login(loginCommand());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().accessToken()).isEqualTo("access-token");
        assertThat(result.getValue().refreshToken()).isEqualTo("refresh-token");
        assertThat(result.getValue().tokenType()).isEqualTo("Bearer");
        assertThat(result.getValue().usuarioId()).isEqualTo(USUARIO_ID);
    }

    @Test
    void login_usuarioNoExiste_retornaCredencialesInvalidas() {
        when(usuarioPort.findByUsername(any(), any())).thenReturn(Optional.empty());

        Result<LoginResponseDto> result = handler.login(loginCommand());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.CREDENCIALES_INVALIDAS);
    }

    @Test
    void login_passwordIncorrecta_retornaCredencialesInvalidas() {
        when(usuarioPort.findByUsername(any(), any())).thenReturn(Optional.of(usuarioActivo()));
        when(passwordEncoder.matches(anyString(), any())).thenReturn(false);

        Result<LoginResponseDto> result = handler.login(loginCommand());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.CREDENCIALES_INVALIDAS);
    }

    @Test
    void login_usuarioBloqueado_retornaUsuarioBloqueado() {
        when(usuarioPort.findByUsername(any(), any())).thenReturn(Optional.of(usuarioBloqueado()));
        when(passwordEncoder.matches(anyString(), any())).thenReturn(true);

        Result<LoginResponseDto> result = handler.login(loginCommand());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_BLOQUEADO);
    }

    @Test
    void login_usuarioInactivo_retornaUsuarioInactivo() {
        when(usuarioPort.findByUsername(any(), any())).thenReturn(Optional.of(usuarioInactivo()));
        when(passwordEncoder.matches(anyString(), any())).thenReturn(true);

        Result<LoginResponseDto> result = handler.login(loginCommand());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_INACTIVO);
    }
}
