package pe.edu.unmsm.ciudadsana.auth.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.auth.application.command.ActualizarPermisoCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
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
class ActualizarPermisoCommandHandlerTest {

    @Mock
    PermisoPersistencePort permisoPort;

    @InjectMocks
    ActualizarPermisoCommandHandler handler;

    private static final UUID PERMISO_ID = UUID.randomUUID();

    @Test
    void actualizar_permisoExistente_actualizaModuloYDescripcion() {
        Permiso existente = Permiso.reconstitute(
                PermisoId.of(PERMISO_ID), "TEST_CODIGO", "MODULO_VIEJO", "descripcion vieja");
        Permiso actualizado = Permiso.reconstitute(
                PermisoId.of(PERMISO_ID), "TEST_CODIGO", "MODULO_NUEVO", "descripcion nueva");

        when(permisoPort.findById(any())).thenReturn(Optional.of(existente));
        when(permisoPort.save(any())).thenReturn(actualizado);

        Result<PermisoResponseDto> result = handler.actualizar(
                new ActualizarPermisoCommand(PERMISO_ID, "MODULO_NUEVO", "descripcion nueva"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().modulo()).isEqualTo("MODULO_NUEVO");
        assertThat(result.getValue().descripcion()).isEqualTo("descripcion nueva");
        assertThat(result.getValue().codigo()).isEqualTo("TEST_CODIGO");
    }

    @Test
    void actualizar_preservaCodigoOriginal() {
        Permiso existente = Permiso.reconstitute(
                PermisoId.of(PERMISO_ID), "CODIGO_FIJO", "MOD", "desc");
        Permiso savedConNuevoModulo = Permiso.reconstitute(
                PermisoId.of(PERMISO_ID), "CODIGO_FIJO", "MOD_NUEVO", "desc nueva");

        when(permisoPort.findById(any())).thenReturn(Optional.of(existente));
        when(permisoPort.save(any())).thenReturn(savedConNuevoModulo);

        Result<PermisoResponseDto> result = handler.actualizar(
                new ActualizarPermisoCommand(PERMISO_ID, "MOD_NUEVO", "desc nueva"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().codigo()).isEqualTo("CODIGO_FIJO");
    }

    @Test
    void actualizar_permisoNoExiste_retornaNoEncontrado() {
        when(permisoPort.findById(any())).thenReturn(Optional.empty());

        Result<PermisoResponseDto> result = handler.actualizar(
                new ActualizarPermisoCommand(PERMISO_ID, "MOD", "desc"));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.PERMISO_NO_ENCONTRADO);
    }
}
