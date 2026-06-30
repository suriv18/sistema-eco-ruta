package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarZonaCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
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
class DesactivarZonaCommandHandlerTest {

    @Mock
    ZonasPersistencePort zonasPersistencePort;

    @Mock
    OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    DesactivarZonaCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ZONA_ID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    @Test
    void desactivar_zona_activa_retorna_success() {
        Zona zona = mock(Zona.class);
        when(zonasPersistencePort.findById(ZonaId.of(ZONA_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(zona));
        when(zona.pullDomainEvents()).thenReturn(List.of());

        DesactivarZonaCommand command = new DesactivarZonaCommand(ZONA_ID, TENANT_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isSuccess()).isTrue();
        verify(zona).desactivar();
        verify(zonasPersistencePort).save(zona);
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void desactivar_zona_no_encontrada_retorna_failure() {
        when(zonasPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        DesactivarZonaCommand command = new DesactivarZonaCommand(ZONA_ID, TENANT_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.ZONA_NO_ENCONTRADA);
        verify(zonasPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }

    @Test
    void desactivar_zona_ya_inactiva_retorna_operacion_no_permitida() {
        Zona zona = mock(Zona.class);
        when(zonasPersistencePort.findById(any(), any())).thenReturn(Optional.of(zona));
        doThrow(new IllegalStateException("La zona ya está INACTIVA"))
                .when(zona).desactivar();

        DesactivarZonaCommand command = new DesactivarZonaCommand(ZONA_ID, TENANT_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.OPERACION_NO_PERMITIDA);
        verify(zonasPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }
}
