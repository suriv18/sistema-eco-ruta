package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.ActualizarRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
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
class ActualizarRolCommandHandlerTest {

    @Mock
    RolPersistencePort rolPort;

    @InjectMocks
    ActualizarRolCommandHandler handler;

    private static final UUID ID = UUID.randomUUID();

    @Test
    void actualizar_rolExistente_retornaDto() {
        Rol rol = Rol.reconstitute(RolId.of(ID), "SUPERVISOR", "Supervisor", "desc", true);
        when(rolPort.findById(any())).thenReturn(Optional.of(rol));
        when(rolPort.save(any())).thenReturn(rol);

        Result<RolResponseDto> result = handler.actualizar(
                new ActualizarRolCommand(ID, "Supervisor Senior", "desc actualizada"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().codigo()).isEqualTo("SUPERVISOR");
        assertThat(result.getValue().activo()).isTrue();
    }

    @Test
    void actualizar_rolNoExiste_retornaNoEncontrado() {
        when(rolPort.findById(any())).thenReturn(Optional.empty());

        Result<RolResponseDto> result = handler.actualizar(
                new ActualizarRolCommand(ID, "Nombre", "desc"));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ROL_NO_ENCONTRADO);
    }
}
