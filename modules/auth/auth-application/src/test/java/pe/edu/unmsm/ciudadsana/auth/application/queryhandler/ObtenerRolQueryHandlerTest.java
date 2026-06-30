package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerRolQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerRolQueryHandlerTest {

    @Mock
    RolPersistencePort rolPort;

    @InjectMocks
    ObtenerRolQueryHandler handler;

    private static final UUID ROL_ID = UUID.randomUUID();

    @Test
    void obtener_rolExistente_retornaDto() {
        Rol rol = Rol.reconstitute(RolId.of(ROL_ID), "SUPERVISOR", "Supervisor", "Supervisor de operaciones", true);
        when(rolPort.findById(any())).thenReturn(Optional.of(rol));

        Result<RolResponseDto> result = handler.obtener(new ObtenerRolQuery(ROL_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(ROL_ID);
        assertThat(result.getValue().codigo()).isEqualTo("SUPERVISOR");
        assertThat(result.getValue().nombre()).isEqualTo("Supervisor");
        assertThat(result.getValue().activo()).isTrue();
    }

    @Test
    void obtener_rolNoExiste_retornaNoEncontrado() {
        when(rolPort.findById(any())).thenReturn(Optional.empty());

        Result<RolResponseDto> result = handler.obtener(new ObtenerRolQuery(ROL_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ROL_NO_ENCONTRADO);
    }
}
