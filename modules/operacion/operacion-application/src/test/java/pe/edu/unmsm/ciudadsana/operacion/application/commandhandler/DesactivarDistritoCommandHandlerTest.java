package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarDistritoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DesactivarDistritoCommandHandlerTest {

    @Mock
    DistritosPersistencePort distritosPersistencePort;

    @InjectMocks
    DesactivarDistritoCommandHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Distrito de La Victoria - zona de alta generación residuos industriales/comerciales
    private static final UUID DISTRITO_LA_VICTORIA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void desactivar_distrito_activo_retorna_success() {
        Distrito distrito = mock(Distrito.class);
        when(distritosPersistencePort.findById(DistritoId.of(DISTRITO_LA_VICTORIA_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(distrito));

        DesactivarDistritoCommand command = new DesactivarDistritoCommand(DISTRITO_LA_VICTORIA_ID, TENANT_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isSuccess()).isTrue();
        verify(distrito).desactivar();
        verify(distritosPersistencePort).save(distrito);
    }

    @Test
    void desactivar_distrito_no_encontrado_retorna_failure() {
        when(distritosPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        DesactivarDistritoCommand command = new DesactivarDistritoCommand(DISTRITO_LA_VICTORIA_ID, TENANT_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.DISTRITO_NO_ENCONTRADO);
        verify(distritosPersistencePort, never()).save(any());
    }

    @Test
    void desactivar_distrito_ya_inactivo_retorna_validacion_error() {
        Distrito distrito = mock(Distrito.class);
        when(distritosPersistencePort.findById(any(), any())).thenReturn(Optional.of(distrito));
        doThrow(new IllegalStateException("El distrito ya está INACTIVO"))
                .when(distrito).desactivar();

        DesactivarDistritoCommand command = new DesactivarDistritoCommand(DISTRITO_LA_VICTORIA_ID, TENANT_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
        verify(distritosPersistencePort, never()).save(any());
    }
}
