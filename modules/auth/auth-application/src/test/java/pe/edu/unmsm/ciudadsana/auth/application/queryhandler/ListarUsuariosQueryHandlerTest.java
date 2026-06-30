package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarUsuariosQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.enums.EstadoUsuario;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Email;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.NombresCompletos;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PasswordHash;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.Username;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarUsuariosQueryHandlerTest {

    @Mock
    UsuarioPersistencePort usuarioPersistence;

    @InjectMocks
    ListarUsuariosQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private Usuario usuarioActivo(String username, String email) {
        return Usuario.reconstitute(
                UsuarioId.of(UUID.randomUUID()),
                TenantId.of(TENANT_ID),
                NombresCompletos.of("Ana", "Garcia"),
                Email.of(email),
                Username.of(username),
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
    void listar_conUsuarios_retornaPaginaConDtos() {
        List<Usuario> usuarios = List.of(
                usuarioActivo("ana", "ana@test.com"),
                usuarioActivo("pedro", "pedro@test.com")
        );
        PageResult<Usuario> page = PageResult.of(usuarios, 0, 10, 2L);
        when(usuarioPersistence.findAll(any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<UsuarioResponseDto>> result = handler.listar(
                new ListarUsuariosQuery(TENANT_ID, 0, 10));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(2);
        assertThat(result.getValue().totalElements()).isEqualTo(2L);
        assertThat(result.getValue().content().get(0).username()).isEqualTo("ana");
    }

    @Test
    void listar_sinUsuarios_retornaPaginaVacia() {
        PageResult<Usuario> page = PageResult.of(List.of(), 0, 10, 0L);
        when(usuarioPersistence.findAll(any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<UsuarioResponseDto>> result = handler.listar(
                new ListarUsuariosQuery(TENANT_ID, 0, 10));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
        assertThat(result.getValue().totalElements()).isZero();
    }
}
