package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarRolesQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarRolesQueryHandlerTest {

    @Mock
    RolPersistencePort rolPort;

    @InjectMocks
    ListarRolesQueryHandler handler;

    @Test
    void listar_conRolesExistentes_retornaPaginaConDtos() {
        List<Rol> roles = List.of(
                Rol.reconstitute(RolId.of(UUID.randomUUID()), "ADMIN", "Administrador", "Admin sistema", true),
                Rol.reconstitute(RolId.of(UUID.randomUUID()), "SUPERVISOR", "Supervisor", "Sup operaciones", false)
        );
        PageResult<Rol> page = PageResult.of(roles, 0, 20, 2L);
        when(rolPort.findAll(0, 20)).thenReturn(page);

        Result<PageResult<RolResponseDto>> result = handler.listar(new ListarRolesQuery(0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(2);
        assertThat(result.getValue().totalElements()).isEqualTo(2L);
        assertThat(result.getValue().content().get(0).codigo()).isEqualTo("ADMIN");
        assertThat(result.getValue().content().get(1).activo()).isFalse();
    }

    @Test
    void listar_sinRoles_retornaPaginaVacia() {
        PageResult<Rol> page = PageResult.empty(0, 20);
        when(rolPort.findAll(0, 20)).thenReturn(page);

        Result<PageResult<RolResponseDto>> result = handler.listar(new ListarRolesQuery(0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
