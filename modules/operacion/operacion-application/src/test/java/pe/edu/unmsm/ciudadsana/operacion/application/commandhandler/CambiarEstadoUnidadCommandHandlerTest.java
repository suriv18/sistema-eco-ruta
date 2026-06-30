package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoUnidadCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.List;
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
class CambiarEstadoUnidadCommandHandlerTest {

    @Mock
    UnidadesPersistencePort unidadesPersistencePort;

    @Mock
    OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    CambiarEstadoUnidadCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNIDAD_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @Test
    void cambiar_estado_unidad_exitoso_retorna_success() {
        Unidad unidad = mock(Unidad.class);
        when(unidadesPersistencePort.findById(UnidadId.of(UNIDAD_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(unidad));
        when(unidad.pullDomainEvents()).thenReturn(List.of());

        CambiarEstadoUnidadCommand command =
                new CambiarEstadoUnidadCommand(UNIDAD_ID, TENANT_ID, "MANTENIMIENTO");

        Result<Void> result = handler.cambiarEstado(command);

        assertThat(result.isSuccess()).isTrue();
        verify(unidad).cambiarEstadoOperativo(any());
        verify(unidadesPersistencePort).save(unidad);
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void cambiar_estado_unidad_no_encontrada_retorna_failure() {
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        CambiarEstadoUnidadCommand command =
                new CambiarEstadoUnidadCommand(UNIDAD_ID, TENANT_ID, "MANTENIMIENTO");

        Result<Void> result = handler.cambiarEstado(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.UNIDAD_NO_ENCONTRADA);
        verify(unidadesPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }

    @Test
    void cambiar_estado_unidad_estado_invalido_retorna_validacion_error() {
        Unidad unidad = mock(Unidad.class);
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.of(unidad));

        CambiarEstadoUnidadCommand command =
                new CambiarEstadoUnidadCommand(UNIDAD_ID, TENANT_ID, "ESTADO_INEXISTENTE");

        Result<Void> result = handler.cambiarEstado(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
        verify(unidadesPersistencePort, never()).save(any());
    }

    @Test
    void cambiar_estado_unidad_mismo_estado_retorna_operacion_no_permitida() {
        Unidad unidad = mock(Unidad.class);
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.of(unidad));
        doThrow(new IllegalStateException("La unidad ya está en estado OPERATIVA"))
                .when(unidad).cambiarEstadoOperativo(any());

        CambiarEstadoUnidadCommand command =
                new CambiarEstadoUnidadCommand(UNIDAD_ID, TENANT_ID, "OPERATIVA");

        Result<Void> result = handler.cambiarEstado(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.OPERACION_NO_PERMITIDA);
        verify(unidadesPersistencePort, never()).save(any());
    }
}
