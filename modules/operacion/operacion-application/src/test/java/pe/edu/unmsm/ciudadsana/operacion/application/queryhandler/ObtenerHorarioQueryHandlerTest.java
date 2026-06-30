package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerHorarioQuery;
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
class ObtenerHorarioQueryHandlerTest {

    @Mock
    HorariosPersistencePort horariosPersistencePort;

    @InjectMocks
    ObtenerHorarioQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID HORARIO_ID = UUID.randomUUID();
    private static final UUID ZONA_ID = UUID.randomUUID();

    private HorarioRecoleccion horarioActivo() {
        return HorarioRecoleccion.reconstitute(
                HorarioRecoleccionId.of(HORARIO_ID), TenantId.of(TENANT_ID), ZonaId.of(ZONA_ID),
                2, LocalTime.of(7, 0), LocalTime.of(11, 0), "martes", EstadoHorario.ACTIVO,
                Instant.now(), null);
    }

    @Test
    void obtener_horarioExistente_retornaDto() {
        when(horariosPersistencePort.findById(any(), any())).thenReturn(Optional.of(horarioActivo()));

        Result<HorarioResponseDto> result = handler.obtener(new ObtenerHorarioQuery(HORARIO_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(HORARIO_ID);
        assertThat(result.getValue().tenantId()).isEqualTo(TENANT_ID);
        assertThat(result.getValue().zonaId()).isEqualTo(ZONA_ID);
        assertThat(result.getValue().diaSemana()).isEqualTo(2);
        assertThat(result.getValue().horaInicio()).isEqualTo(LocalTime.of(7, 0));
        assertThat(result.getValue().estado()).isEqualTo("ACTIVO");
    }

    @Test
    void obtener_horarioNoExiste_retornaNoEncontrado() {
        when(horariosPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        Result<HorarioResponseDto> result = handler.obtener(new ObtenerHorarioQuery(HORARIO_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.HORARIO_NO_ENCONTRADO);
    }
}
