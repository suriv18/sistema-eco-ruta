package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CancelarTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
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
class CancelarTurnoCommandHandlerTest {

    @Mock
    TurnosPersistencePort turnosPersistencePort;

    @InjectMocks
    CancelarTurnoCommandHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Turno matutino (05:00-13:00) - ruta recolección San Juan de Lurigancho
    private static final UUID TURNO_MANANA_SJL_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void cancelar_turno_activo_retorna_success() {
        Turno turno = mock(Turno.class);
        when(turnosPersistencePort.findById(TurnoId.of(TURNO_MANANA_SJL_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(turno));

        CancelarTurnoCommand command = new CancelarTurnoCommand(TURNO_MANANA_SJL_ID, TENANT_ID);

        Result<Void> result = handler.cancelar(command);

        assertThat(result.isSuccess()).isTrue();
        verify(turno).cancelar();
        verify(turnosPersistencePort).save(turno);
    }

    @Test
    void cancelar_turno_no_encontrado_retorna_failure() {
        when(turnosPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        CancelarTurnoCommand command = new CancelarTurnoCommand(TURNO_MANANA_SJL_ID, TENANT_ID);

        Result<Void> result = handler.cancelar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.TURNO_NO_ENCONTRADO);
        verify(turnosPersistencePort, never()).save(any());
    }

    @Test
    void cancelar_turno_estado_invalido_retorna_turno_invalido() {
        Turno turno = mock(Turno.class);
        when(turnosPersistencePort.findById(any(), any())).thenReturn(Optional.of(turno));
        doThrow(new IllegalStateException("El turno no puede ser cancelado en su estado actual"))
                .when(turno).cancelar();

        CancelarTurnoCommand command = new CancelarTurnoCommand(TURNO_MANANA_SJL_ID, TENANT_ID);

        Result<Void> result = handler.cancelar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.TURNO_INVALIDO);
        verify(turnosPersistencePort, never()).save(any());
    }
}
