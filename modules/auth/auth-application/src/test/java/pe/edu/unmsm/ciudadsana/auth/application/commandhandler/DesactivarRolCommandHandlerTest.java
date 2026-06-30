package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DesactivarRolCommandHandlerTest {

    @Mock
    RolPersistencePort rolPort;

    @InjectMocks
    DesactivarRolCommandHandler handler;

    private static final UUID ROL_ID = UUID.randomUUID();

    @Test
    void desactivar_rolExistente_retornaSuccess() {
        Rol rol = Rol.reconstitute(RolId.of(ROL_ID), "SUPERVISOR", "Supervisor", "desc", true);
        when(rolPort.findById(any())).thenReturn(Optional.of(rol));
        when(rolPort.save(any())).thenReturn(rol);

        Result<Void> result = handler.desactivar(ROL_ID);

        assertThat(result.isSuccess()).isTrue();
        verify(rolPort).save(any());
    }

    @Test
    void desactivar_rolNoExiste_retornaNoEncontrado() {
        when(rolPort.findById(any())).thenReturn(Optional.empty());

        Result<Void> result = handler.desactivar(ROL_ID);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ROL_NO_ENCONTRADO);
    }
}
