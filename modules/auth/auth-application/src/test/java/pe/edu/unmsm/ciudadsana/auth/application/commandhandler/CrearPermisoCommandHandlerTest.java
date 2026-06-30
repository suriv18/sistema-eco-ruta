package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.CrearPermisoCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearPermisoCommandHandlerTest {

    @Mock
    PermisoPersistencePort permisoPort;

    @InjectMocks
    CrearPermisoCommandHandler handler;

    @Test
    void crear_codigoNuevo_retornaDto() {
        Permiso saved = Permiso.reconstitute(
                PermisoId.of(UUID.randomUUID()), "LEER_USUARIO", "USUARIOS", "Permite leer usuarios");
        when(permisoPort.existsByCodigo("LEER_USUARIO")).thenReturn(false);
        when(permisoPort.save(any())).thenReturn(saved);

        Result<PermisoResponseDto> result = handler.crear(
                new CrearPermisoCommand("LEER_USUARIO", "USUARIOS", "Permite leer usuarios"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().codigo()).isEqualTo("LEER_USUARIO");
        assertThat(result.getValue().modulo()).isEqualTo("USUARIOS");
    }

    @Test
    void crear_codigoDuplicado_retornaDuplicado() {
        when(permisoPort.existsByCodigo("LEER_USUARIO")).thenReturn(true);

        Result<PermisoResponseDto> result = handler.crear(
                new CrearPermisoCommand("LEER_USUARIO", "USUARIOS", "desc"));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.PERMISO_DUPLICADO);
    }
}
