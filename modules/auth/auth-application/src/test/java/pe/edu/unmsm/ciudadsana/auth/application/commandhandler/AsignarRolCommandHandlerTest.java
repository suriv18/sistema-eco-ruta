package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.AsignarRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.enums.EstadoUsuario;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Email;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.NombresCompletos;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsignarRolCommandHandlerTest {

    @Mock
    UsuarioPersistencePort usuarioPersistence;

    @Mock
    RolPersistencePort rolPort;

    @Mock
    EventPublisherPort eventPublisher;

    @InjectMocks
    AsignarRolCommandHandler handler;

    private static final UUID USUARIO_ID = UUID.randomUUID();
    private static final UUID ROL_ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

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
    void asignarRol_usuarioYRolExisten_retornaSuccess() {
        Rol rol = Rol.reconstitute(RolId.of(ROL_ID), "AUDITOR", "Auditor", "desc", true);
        when(usuarioPersistence.findById(any())).thenReturn(Optional.of(usuarioActivo()));
        when(rolPort.findById(any())).thenReturn(Optional.of(rol));
        when(usuarioPersistence.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Result<Void> result = handler.asignarRol(new AsignarRolCommand(USUARIO_ID, ROL_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        verify(usuarioPersistence).save(any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void asignarRol_usuarioNoExiste_retornaNoEncontrado() {
        when(usuarioPersistence.findById(any())).thenReturn(Optional.empty());

        Result<Void> result = handler.asignarRol(new AsignarRolCommand(USUARIO_ID, ROL_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_NO_ENCONTRADO);
    }

    @Test
    void asignarRol_rolNoExiste_retornaNoEncontrado() {
        when(usuarioPersistence.findById(any())).thenReturn(Optional.of(usuarioActivo()));
        when(rolPort.findById(any())).thenReturn(Optional.empty());

        Result<Void> result = handler.asignarRol(new AsignarRolCommand(USUARIO_ID, ROL_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ROL_NO_ENCONTRADO);
    }
}
