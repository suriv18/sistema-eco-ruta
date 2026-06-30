package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.ActualizarHorarioCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActualizarHorarioCommandHandlerTest {

    @Mock
    HorariosPersistencePort horariosPersistencePort;

    @InjectMocks
    ActualizarHorarioCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID HORARIO_ID = UUID.randomUUID();
    private static final UUID ZONA_ID = UUID.randomUUID();

    @Test
    void actualizar_conRangoValido_retornaHorarioActualizado() {
        HorarioRecoleccion existente = HorarioRecoleccion.reconstitute(
                HorarioRecoleccionId.of(HORARIO_ID), TenantId.of(TENANT_ID), ZonaId.of(ZONA_ID),
                1, LocalTime.of(8, 0), LocalTime.of(12, 0), "original", EstadoHorario.ACTIVO,
                Instant.now(), null);
        HorarioRecoleccion guardado = HorarioRecoleccion.reconstitute(
                HorarioRecoleccionId.of(HORARIO_ID), TenantId.of(TENANT_ID), ZonaId.of(ZONA_ID),
                1, LocalTime.of(9, 0), LocalTime.of(13, 0), "actualizado", EstadoHorario.ACTIVO,
                Instant.now(), Instant.now());

        when(horariosPersistencePort.findById(any(), any())).thenReturn(Optional.of(existente));
        when(horariosPersistencePort.save(any())).thenReturn(guardado);

        ActualizarHorarioCommand cmd = new ActualizarHorarioCommand(
                TENANT_ID, HORARIO_ID, LocalTime.of(9, 0), LocalTime.of(13, 0), "actualizado");

        Result<HorarioResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().horaInicio()).isEqualTo(LocalTime.of(9, 0));
        assertThat(result.getValue().horaFin()).isEqualTo(LocalTime.of(13, 0));
        assertThat(result.getValue().observacion()).isEqualTo("actualizado");
    }

    @Test
    void actualizar_conHoraFinAnteriorAInicio_retornaRangoInvalido() {
        ActualizarHorarioCommand cmd = new ActualizarHorarioCommand(
                TENANT_ID, HORARIO_ID, LocalTime.of(12, 0), LocalTime.of(8, 0), null);

        Result<HorarioResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.HORARIO_RANGO_INVALIDO);
    }

    @Test
    void actualizar_conHoraFinIgualAInicio_retornaRangoInvalido() {
        ActualizarHorarioCommand cmd = new ActualizarHorarioCommand(
                TENANT_ID, HORARIO_ID, LocalTime.of(8, 0), LocalTime.of(8, 0), null);

        Result<HorarioResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.HORARIO_RANGO_INVALIDO);
    }

    @Test
    void actualizar_horarioNoExiste_retornaNoEncontrado() {
        when(horariosPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        ActualizarHorarioCommand cmd = new ActualizarHorarioCommand(
                TENANT_ID, HORARIO_ID, LocalTime.of(8, 0), LocalTime.of(12, 0), null);

        Result<HorarioResponseDto> result = handler.actualizar(cmd);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.HORARIO_NO_ENCONTRADO);
    }
}
