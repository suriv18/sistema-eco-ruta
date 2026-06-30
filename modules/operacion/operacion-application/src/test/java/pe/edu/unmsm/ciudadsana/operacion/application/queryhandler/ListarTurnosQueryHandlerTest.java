package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarTurnosQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarTurnosQueryHandlerTest {

    @Mock
    private TurnosPersistencePort turnosPersistencePort;

    @InjectMocks
    private ListarTurnosQueryHandler handler;

    @Test
    void listar_con_elementos_retorna_page_result_con_dtos() {
        UUID tenantUuid = UUID.randomUUID();
        UUID turnoUuid = UUID.randomUUID();
        UUID unidadUuid = UUID.randomUUID();
        UUID choferUuid = UUID.randomUUID();
        UUID distritoUuid = UUID.randomUUID();
        Instant ahora = Instant.now();
        LocalDate fecha = LocalDate.of(2026, 1, 15);
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fin = LocalTime.of(12, 0);

        Turno turno = mock(Turno.class);
        when(turno.getId()).thenReturn(TurnoId.of(turnoUuid));
        when(turno.getTenantId()).thenReturn(TenantId.of(tenantUuid));
        when(turno.getUnidadId()).thenReturn(UnidadId.of(unidadUuid));
        when(turno.getChoferId()).thenReturn(ChoferId.of(choferUuid));
        when(turno.getDistritoId()).thenReturn(DistritoId.of(distritoUuid));
        when(turno.getFecha()).thenReturn(fecha);
        when(turno.getHoraInicio()).thenReturn(inicio);
        when(turno.getHoraFin()).thenReturn(fin);
        when(turno.getTipo()).thenReturn(TipoTurno.MANANA);
        when(turno.getEstado()).thenReturn(EstadoTurno.PROGRAMADO);
        when(turno.getCreadoEn()).thenReturn(ahora);

        PageResult<Turno> pageResult = PageResult.of(List.of(turno), 0, 10, 1L);
        when(turnosPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageResult);

        ListarTurnosQuery query = new ListarTurnosQuery(tenantUuid, 0, 10);
        Result<PageResult<TurnoResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<TurnoResponseDto> page = resultado.getValue();
        assertThat(page.content()).hasSize(1);
        assertThat(page.totalElements()).isEqualTo(1L);
        TurnoResponseDto dto = page.content().getFirst();
        assertThat(dto.id()).isEqualTo(turnoUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.unidadId()).isEqualTo(unidadUuid);
        assertThat(dto.choferId()).isEqualTo(choferUuid);
        assertThat(dto.distritoId()).isEqualTo(distritoUuid);
        assertThat(dto.fecha()).isEqualTo(fecha);
        assertThat(dto.tipoTurno()).isEqualTo("MANANA");
        assertThat(dto.estado()).isEqualTo("PROGRAMADO");
    }

    @Test
    void listar_sin_elementos_retorna_page_result_vacio() {
        UUID tenantUuid = UUID.randomUUID();
        PageResult<Turno> pageVacio = PageResult.empty(0, 10);
        when(turnosPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageVacio);

        ListarTurnosQuery query = new ListarTurnosQuery(tenantUuid, 0, 10);
        Result<PageResult<TurnoResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<TurnoResponseDto> page = resultado.getValue();
        assertThat(page.content()).isEmpty();
        assertThat(page.totalElements()).isZero();
    }
}
