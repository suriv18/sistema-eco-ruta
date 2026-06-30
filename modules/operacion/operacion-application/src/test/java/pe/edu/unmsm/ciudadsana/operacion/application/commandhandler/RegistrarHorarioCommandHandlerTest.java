package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarHorarioCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoHorario;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarHorarioCommandHandlerTest {

    @Mock
    private HorariosPersistencePort horariosPersistencePort;

    @Mock
    private OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    private RegistrarHorarioCommandHandler handler;

    @Test
    void registrar_horario_valido_retorna_success() {
        UUID tenantId = UUID.randomUUID();
        UUID zonaId = UUID.randomUUID();
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fin = LocalTime.of(10, 0);
        RegistrarHorarioCommand command = new RegistrarHorarioCommand(
            tenantId, zonaId, 1, inicio, fin, "Sin observaciones"
        );
        HorarioRecoleccion horarioGuardado = HorarioRecoleccion.reconstitute(
            HorarioRecoleccionId.of(UUID.randomUUID()),
            TenantId.of(tenantId),
            ZonaId.of(zonaId),
            1, inicio, fin,
            "Sin observaciones",
            EstadoHorario.ACTIVO,
            Instant.now(),
            null
        );
        when(horariosPersistencePort.existsByZonaAndDiaAndHorario(
            any(ZonaId.class), anyInt(), any(LocalTime.class), any(LocalTime.class), any(TenantId.class)
        )).thenReturn(false);
        when(horariosPersistencePort.save(any(HorarioRecoleccion.class))).thenReturn(horarioGuardado);
        doNothing().when(eventPublisher).publishAll(any(List.class));

        Result<HorarioResponseDto> result = handler.registrar(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().diaSemana()).isEqualTo(1);
        assertThat(result.getValue().estado()).isEqualTo("ACTIVO");
    }

    @Test
    void registrar_horario_duplicado_retorna_horario_duplicado() {
        UUID tenantId = UUID.randomUUID();
        UUID zonaId = UUID.randomUUID();
        RegistrarHorarioCommand command = new RegistrarHorarioCommand(
            tenantId, zonaId, 2, LocalTime.of(9, 0), LocalTime.of(11, 0), null
        );
        when(horariosPersistencePort.existsByZonaAndDiaAndHorario(
            any(ZonaId.class), anyInt(), any(LocalTime.class), any(LocalTime.class), any(TenantId.class)
        )).thenReturn(true);

        Result<HorarioResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.HORARIO_DUPLICADO);
    }

    @Test
    void registrar_horario_rango_invalido_retorna_horario_rango_invalido() {
        UUID tenantId = UUID.randomUUID();
        UUID zonaId = UUID.randomUUID();
        LocalTime inicio = LocalTime.of(12, 0);
        LocalTime fin = LocalTime.of(10, 0);
        RegistrarHorarioCommand command = new RegistrarHorarioCommand(
            tenantId, zonaId, 3, inicio, fin, null
        );
        when(horariosPersistencePort.existsByZonaAndDiaAndHorario(
            any(ZonaId.class), anyInt(), any(LocalTime.class), any(LocalTime.class), any(TenantId.class)
        )).thenReturn(false);

        Result<HorarioResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.HORARIO_RANGO_INVALIDO);
    }
}
