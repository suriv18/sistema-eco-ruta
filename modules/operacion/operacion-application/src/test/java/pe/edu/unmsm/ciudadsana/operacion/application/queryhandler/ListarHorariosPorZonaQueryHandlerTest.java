package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarHorariosPorZonaQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoHorario;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarHorariosPorZonaQueryHandlerTest {

    @Mock
    HorariosPersistencePort horariosPersistencePort;

    @InjectMocks
    ListarHorariosPorZonaQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ZONA_ID = UUID.randomUUID();

    private HorarioRecoleccion horario(int dia) {
        return HorarioRecoleccion.reconstitute(
                HorarioRecoleccionId.of(UUID.randomUUID()), TenantId.of(TENANT_ID), ZonaId.of(ZONA_ID),
                dia, LocalTime.of(8, 0), LocalTime.of(12, 0), null, EstadoHorario.ACTIVO,
                Instant.now(), null);
    }

    @Test
    void listar_conHorariosEnZona_retornaPaginaConDtos() {
        List<HorarioRecoleccion> horarios = List.of(horario(1), horario(3));
        PageResult<HorarioRecoleccion> page = PageResult.of(horarios, 0, 20, 2L);
        when(horariosPersistencePort.findAllByZona(any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<HorarioResponseDto>> result = handler.listar(
                new ListarHorariosPorZonaQuery(TENANT_ID, ZONA_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(2);
        assertThat(result.getValue().totalElements()).isEqualTo(2L);
        assertThat(result.getValue().content().get(0).diaSemana()).isEqualTo(1);
        assertThat(result.getValue().content().get(1).diaSemana()).isEqualTo(3);
    }

    @Test
    void listar_sinHorariosEnZona_retornaPaginaVacia() {
        PageResult<HorarioRecoleccion> page = PageResult.empty(0, 20);
        when(horariosPersistencePort.findAllByZona(any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<HorarioResponseDto>> result = handler.listar(
                new ListarHorariosPorZonaQuery(TENANT_ID, ZONA_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
