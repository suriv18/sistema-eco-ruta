package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerCurrentUserQuery;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerCurrentUserQueryHandlerTest {

    @Mock
    UsuarioPersistencePort usuarioPersistence;

    @InjectMocks
    ObtenerCurrentUserQueryHandler handler;

    private static final UUID USUARIO_ID = UUID.randomUUID();
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private Usuario usuarioActivo() {
        return Usuario.reconstitute(
                UsuarioId.of(USUARIO_ID),
                TenantId.of(TENANT_ID),
                new NombresCompletos("Admin", "Municipal"),
                new Email("admin@ciudadsana.pe"),
                new Username("admin"),
                PasswordHash.of("$2b$10$hash"),
                null,
                EstadoUsuario.ACTIVO,
                null,
                Instant.now(),
                null,
                Set.of()
        );
    }

    @Test
    void obtenerCurrentUser_usuarioExistenteEnMismoTenant_retornaDto() {
        when(usuarioPersistence.findById(any())).thenReturn(Optional.of(usuarioActivo()));

        Result<UsuarioResponseDto> result = handler.obtenerCurrentUser(
                new ObtenerCurrentUserQuery(USUARIO_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().usuarioId()).isEqualTo(USUARIO_ID);
        assertThat(result.getValue().tenantId()).isEqualTo(TENANT_ID);
        assertThat(result.getValue().nombres()).isEqualTo("Admin");
        assertThat(result.getValue().username()).isEqualTo("admin");
    }

    @Test
    void obtenerCurrentUser_usuarioNoExiste_retornaNoEncontrado() {
        when(usuarioPersistence.findById(any())).thenReturn(Optional.empty());

        Result<UsuarioResponseDto> result = handler.obtenerCurrentUser(
                new ObtenerCurrentUserQuery(USUARIO_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_NO_ENCONTRADO);
    }

    @Test
    void obtenerCurrentUser_tenantDistinto_retornaSinPermiso() {
        when(usuarioPersistence.findById(any())).thenReturn(Optional.of(usuarioActivo()));

        UUID otroTenant = UUID.randomUUID();
        Result<UsuarioResponseDto> result = handler.obtenerCurrentUser(
                new ObtenerCurrentUserQuery(USUARIO_ID, otroTenant));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.USUARIO_SIN_PERMISO);
    }
}
