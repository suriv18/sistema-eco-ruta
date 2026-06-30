package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.ActivarUsuarioCommand;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.enums.EstadoUsuario;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivarUsuarioCommandHandlerTest {

    @Mock
    UsuarioPersistencePort usuarioPersistence;

    @Mock
    EventPublisherPort eventPublisher;

    @InjectMocks
    ActivarUsuarioCommandHandler handler;

    private static final UUID ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private Usuario usuarioBloqueado() {
        return Usuario.reconstitute(
                UsuarioId.of(ID),
                TenantId.of(TENANT_ID),
                NombresCompletos.of("Ana", "Garcia"),
                Email.of("ana@test.com"),
                Username.of("ana"),
                PasswordHash.of("hash"),
                null,
                EstadoUsuario.BLOQUEADO,
                null,
                Instant.now(),
                null,
                Set.of()
        );
    }

    @Test
    void activar_usuarioBloqueado_mismoTenant_retornaSuccess() {
        when(usuarioPersistence.findById(any())).thenReturn(Optional.of(usuarioBloqueado()));
        when(usuarioPersistence.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Result<Void> result = handler.activar(new ActivarUsuarioCommand(ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        verify(usuarioPersistence).save(any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void activar_usuarioNoExiste_retornaNoEncontrado() {
        when(usuarioPersistence.findById(any())).thenReturn(Optional.empty());

        Result<Void> result = handler.activar(new ActivarUsuarioCommand(ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_NO_ENCONTRADO);
    }

    @Test
    void activar_tenantDistinto_retornaSinPermiso() {
        when(usuarioPersistence.findById(any())).thenReturn(Optional.of(usuarioBloqueado()));

        Result<Void> result = handler.activar(new ActivarUsuarioCommand(ID, UUID.randomUUID()));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_SIN_PERMISO);
    }

    @Test
    void activar_usuarioYaActivo_retornaOperacionNoPermitida() {
        Usuario usuario = mock(Usuario.class);
        when(usuario.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        doThrow(new IllegalStateException("ya activo")).when(usuario).activar();
        when(usuarioPersistence.findById(any())).thenReturn(Optional.of(usuario));

        Result<Void> result = handler.activar(new ActivarUsuarioCommand(ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.OPERACION_NO_PERMITIDA);
    }
}
