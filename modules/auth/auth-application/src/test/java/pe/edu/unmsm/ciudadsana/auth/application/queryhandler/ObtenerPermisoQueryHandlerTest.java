package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerPermisoQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerPermisoQueryHandlerTest {

    @Mock
    PermisoPersistencePort permisoPort;

    @InjectMocks
    ObtenerPermisoQueryHandler handler;

    private static final UUID PERMISO_ID = UUID.randomUUID();

    @Test
    void obtener_permisoExistente_retornaDto() {
        Permiso permiso = Permiso.reconstitute(
                PermisoId.of(PERMISO_ID), "GESTIONAR_USUARIOS", "USUARIOS", "Gestionar usuarios del sistema");
        when(permisoPort.findById(any())).thenReturn(Optional.of(permiso));

        Result<PermisoResponseDto> result = handler.obtener(new ObtenerPermisoQuery(PERMISO_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(PERMISO_ID);
        assertThat(result.getValue().codigo()).isEqualTo("GESTIONAR_USUARIOS");
        assertThat(result.getValue().modulo()).isEqualTo("USUARIOS");
    }

    @Test
    void obtener_permisoNoExiste_retornaNoEncontrado() {
        when(permisoPort.findById(any())).thenReturn(Optional.empty());

        Result<PermisoResponseDto> result = handler.obtener(new ObtenerPermisoQuery(PERMISO_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.PERMISO_NO_ENCONTRADO);
    }
}
