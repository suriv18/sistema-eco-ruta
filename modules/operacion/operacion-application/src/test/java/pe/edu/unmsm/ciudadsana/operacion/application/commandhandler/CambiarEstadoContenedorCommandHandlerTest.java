package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoContenedorCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ContenedoresPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Contenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CambiarEstadoContenedorCommandHandlerTest {

    @Mock
    ContenedoresPersistencePort contenedoresPersistencePort;

    @Mock
    OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    CambiarEstadoContenedorCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID CONTENEDOR_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void cambiar_estado_contenedor_exitoso_retorna_success() {
        Contenedor contenedor = mock(Contenedor.class);
        when(contenedoresPersistencePort.findById(ContenedorId.of(CONTENEDOR_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(contenedor));
        when(contenedor.pullDomainEvents()).thenReturn(List.of());

        CambiarEstadoContenedorCommand command =
                new CambiarEstadoContenedorCommand(CONTENEDOR_ID, TENANT_ID, "LLENO");

        Result<Void> result = handler.cambiarEstado(command);

        assertThat(result.isSuccess()).isTrue();
        verify(contenedor).cambiarEstado(any());
        verify(contenedoresPersistencePort).save(contenedor);
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void cambiar_estado_contenedor_no_encontrado_retorna_failure() {
        when(contenedoresPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        CambiarEstadoContenedorCommand command =
                new CambiarEstadoContenedorCommand(CONTENEDOR_ID, TENANT_ID, "LLENO");

        Result<Void> result = handler.cambiarEstado(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.CONTENEDOR_NO_ENCONTRADO);
        verify(contenedoresPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }

    @Test
    void cambiar_estado_contenedor_estado_invalido_retorna_validacion_error() {
        Contenedor contenedor = mock(Contenedor.class);
        when(contenedoresPersistencePort.findById(any(), any())).thenReturn(Optional.of(contenedor));

        CambiarEstadoContenedorCommand command =
                new CambiarEstadoContenedorCommand(CONTENEDOR_ID, TENANT_ID, "ESTADO_DESCONOCIDO");

        Result<Void> result = handler.cambiarEstado(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
        verify(contenedoresPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }

    @Test
    void cambiar_estado_contenedor_operacion_no_permitida_retorna_failure() {
        Contenedor contenedor = mock(Contenedor.class);
        when(contenedoresPersistencePort.findById(any(), any())).thenReturn(Optional.of(contenedor));
        when(contenedor.pullDomainEvents()).thenReturn(List.of());
        org.mockito.Mockito.doThrow(new IllegalStateException("Transicion no permitida"))
                .when(contenedor).cambiarEstado(any());

        CambiarEstadoContenedorCommand command =
                new CambiarEstadoContenedorCommand(CONTENEDOR_ID, TENANT_ID, "VACIO");

        Result<Void> result = handler.cambiarEstado(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.OPERACION_NO_PERMITIDA);
        verify(contenedoresPersistencePort, never()).save(any());
    }
}
