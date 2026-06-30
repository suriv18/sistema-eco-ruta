package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.RegistrarUsuarioCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PasswordEncoderPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioCommandHandlerTest {

    @Mock
    UsuarioPersistencePort usuarioPort;

    @Mock
    PasswordEncoderPort passwordEncoder;

    @Mock
    EventPublisherPort eventPublisher;

    @InjectMocks
    RegistrarUsuarioCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private RegistrarUsuarioCommand comando() {
        return new RegistrarUsuarioCommand(
                TENANT_ID, "Ana", "Garcia", "ana@test.com", "ana", "password123", null);
    }

    @Test
    void registrar_datosValidos_retornaDto() {
        when(usuarioPort.existsByEmail(any(), any())).thenReturn(false);
        when(usuarioPort.existsByUsername(any(), any())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(PasswordHash.of("hashed"));
        when(usuarioPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Result<UsuarioResponseDto> result = handler.registrar(comando());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().nombres()).isEqualTo("Ana");
        assertThat(result.getValue().email()).isEqualTo("ana@test.com");
        assertThat(result.getValue().username()).isEqualTo("ana");
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void registrar_emailDuplicado_retornaYaExiste() {
        when(usuarioPort.existsByEmail(any(), any())).thenReturn(true);

        Result<UsuarioResponseDto> result = handler.registrar(comando());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_YA_EXISTE);
    }

    @Test
    void registrar_usernameDuplicado_retornaYaExiste() {
        when(usuarioPort.existsByEmail(any(), any())).thenReturn(false);
        when(usuarioPort.existsByUsername(any(), any())).thenReturn(true);

        Result<UsuarioResponseDto> result = handler.registrar(comando());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_YA_EXISTE);
    }
}
