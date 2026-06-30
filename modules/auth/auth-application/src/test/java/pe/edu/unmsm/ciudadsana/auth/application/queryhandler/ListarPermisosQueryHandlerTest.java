package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarPermisosQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarPermisosQueryHandlerTest {

    @Mock
    PermisoPersistencePort permisoPort;

    @InjectMocks
    ListarPermisosQueryHandler handler;

    @Test
    void listar_conPermisosExistentes_retornaPaginaConDtos() {
        List<Permiso> permisos = List.of(
                Permiso.reconstitute(PermisoId.of(UUID.randomUUID()), "VER_ALERTAS", "ALERTAS", "Ver alertas"),
                Permiso.reconstitute(PermisoId.of(UUID.randomUUID()), "CREAR_RUTAS", "RUTAS", "Crear rutas")
        );
        PageResult<Permiso> page = PageResult.of(permisos, 0, 20, 2L);
        when(permisoPort.findAll(0, 20)).thenReturn(page);

        Result<PageResult<PermisoResponseDto>> result = handler.listar(new ListarPermisosQuery(0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(2);
        assertThat(result.getValue().totalElements()).isEqualTo(2L);
        assertThat(result.getValue().content().get(0).codigo()).isEqualTo("VER_ALERTAS");
        assertThat(result.getValue().content().get(1).codigo()).isEqualTo("CREAR_RUTAS");
    }

    @Test
    void listar_sinPermisos_retornaPaginaVacia() {
        PageResult<Permiso> page = PageResult.empty(0, 20);
        when(permisoPort.findAll(0, 20)).thenReturn(page);

        Result<PageResult<PermisoResponseDto>> result = handler.listar(new ListarPermisosQuery(0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
        assertThat(result.getValue().totalElements()).isZero();
    }
}
