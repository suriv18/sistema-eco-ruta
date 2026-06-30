package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.IniciarTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
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
class IniciarTurnoCommandHandlerTest {

    @Mock
    TurnosPersistencePort turnosPersistencePort;

    @Mock
    OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    IniciarTurnoCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID TURNO_ID = UUID.fromString("88888888-8888-8888-8888-888888888888");

    @Test
    void iniciar_turno_programado_retorna_success() {
        Turno turno = mock(Turno.class);
        when(turnosPersistencePort.findById(TurnoId.of(TURNO_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(turno));
        when(turno.pullDomainEvents()).thenReturn(List.of());

        IniciarTurnoCommand command = new IniciarTurnoCommand(TURNO_ID, TENANT_ID);

        Result<Void> result = handler.iniciar(command);

        assertThat(result.isSuccess()).isTrue();
        verify(turno).iniciar(any());
        verify(turnosPersistencePort).save(turno);
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void iniciar_turno_no_encontrado_retorna_failure() {
        when(turnosPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        IniciarTurnoCommand command = new IniciarTurnoCommand(TURNO_ID, TENANT_ID);

        Result<Void> result = handler.iniciar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.TURNO_NO_ENCONTRADO);
        verify(turnosPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }

    @Test
    void iniciar_turno_no_programado_retorna_turno_invalido() {
        Turno turno = mock(Turno.class);
        when(turnosPersistencePort.findById(any(), any())).thenReturn(Optional.of(turno));
        doThrow(new IllegalStateException("Solo se puede iniciar un turno PROGRAMADO. Estado actual: EN_CURSO"))
                .when(turno).iniciar(any());

        IniciarTurnoCommand command = new IniciarTurnoCommand(TURNO_ID, TENANT_ID);

        Result<Void> result = handler.iniciar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.TURNO_INVALIDO);
        verify(turnosPersistencePort, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }
}
