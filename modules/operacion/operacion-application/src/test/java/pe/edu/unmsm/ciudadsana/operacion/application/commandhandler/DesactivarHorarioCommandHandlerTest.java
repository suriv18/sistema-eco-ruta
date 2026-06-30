package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarHorarioCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoHorario;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DesactivarHorarioCommandHandlerTest {

    @Mock
    HorariosPersistencePort horariosPersistencePort;

    @InjectMocks
    DesactivarHorarioCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID HORARIO_ID = UUID.fromString("66666666-6666-6666-6666-666666666666");
    private static final UUID ZONA_ID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    @Test
    void desactivar_horario_activo_retorna_success() {
        HorarioRecoleccion horario = HorarioRecoleccion.reconstitute(
                HorarioRecoleccionId.of(HORARIO_ID),
                TenantId.of(TENANT_ID),
                ZonaId.of(ZONA_ID),
                1,
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                null,
                EstadoHorario.ACTIVO,
                Instant.now(),
                null);

        when(horariosPersistencePort.findById(HorarioRecoleccionId.of(HORARIO_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(horario));
        when(horariosPersistencePort.save(any())).thenReturn(horario);

        DesactivarHorarioCommand command = new DesactivarHorarioCommand(TENANT_ID, HORARIO_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isSuccess()).isTrue();
        verify(horariosPersistencePort).save(horario);
    }

    @Test
    void desactivar_horario_no_encontrado_retorna_failure() {
        when(horariosPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        DesactivarHorarioCommand command = new DesactivarHorarioCommand(TENANT_ID, HORARIO_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.HORARIO_NO_ENCONTRADO);
        verify(horariosPersistencePort, never()).save(any());
    }

    @Test
    void desactivar_horario_ya_inactivo_lanza_illegal_state() {
        HorarioRecoleccion horario = HorarioRecoleccion.reconstitute(
                HorarioRecoleccionId.of(HORARIO_ID),
                TenantId.of(TENANT_ID),
                ZonaId.of(ZONA_ID),
                2,
                LocalTime.of(14, 0),
                LocalTime.of(18, 0),
                null,
                EstadoHorario.INACTIVO,
                Instant.now(),
                null);

        when(horariosPersistencePort.findById(any(), any())).thenReturn(Optional.of(horario));

        DesactivarHorarioCommand command = new DesactivarHorarioCommand(TENANT_ID, HORARIO_ID);

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalStateException.class, () -> handler.desactivar(command));
    }
}
