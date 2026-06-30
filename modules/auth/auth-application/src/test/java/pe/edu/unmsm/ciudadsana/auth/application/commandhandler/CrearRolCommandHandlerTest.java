package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.CrearRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearRolCommandHandlerTest {

    @Mock
    RolPersistencePort rolPort;

    @Mock
    EventPublisherPort eventPublisher;

    @InjectMocks
    CrearRolCommandHandler handler;

    @Test
    void crear_rolNuevo_retornaRolCreado() {
        Rol savedRol = Rol.reconstitute(RolId.of(UUID.randomUUID()), "AUDITOR", "Auditor", "desc", true);
        when(rolPort.existsByCodigo("AUDITOR")).thenReturn(false);
        when(rolPort.save(any())).thenReturn(savedRol);

        Result<RolResponseDto> result = handler.crear(new CrearRolCommand("AUDITOR", "Auditor", "desc"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().codigo()).isEqualTo("AUDITOR");
        assertThat(result.getValue().nombre()).isEqualTo("Auditor");
        assertThat(result.getValue().activo()).isTrue();
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void crear_codigoDuplicado_retornaError() {
        when(rolPort.existsByCodigo("ADMIN")).thenReturn(true);

        Result<RolResponseDto> result = handler.crear(new CrearRolCommand("ADMIN", "Admin", null));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ROL_DUPLICADO);
    }
}
