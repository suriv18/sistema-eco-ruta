package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerTurnoQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerTurnoQueryHandlerTest {

    @Mock
    private TurnosPersistencePort turnosPersistencePort;

    @InjectMocks
    private ObtenerTurnoQueryHandler handler;

    @Test
    void obtener_turno_existente_retorna_dto() {
        UUID tenantUuid = UUID.randomUUID();
        UUID turnoUuid = UUID.randomUUID();
        UUID unidadUuid = UUID.randomUUID();
        UUID choferUuid = UUID.randomUUID();
        UUID distritoUuid = UUID.randomUUID();
        Instant ahora = Instant.now();
        LocalDate fecha = LocalDate.of(2026, 3, 10);
        LocalTime inicio = LocalTime.of(6, 0);
        LocalTime fin = LocalTime.of(14, 0);

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
        when(turno.getEstado()).thenReturn(EstadoTurno.EN_CURSO);
        when(turno.getCreadoEn()).thenReturn(ahora);

        when(turnosPersistencePort.findById(any(TurnoId.class), any(TenantId.class)))
            .thenReturn(Optional.of(turno));

        ObtenerTurnoQuery query = new ObtenerTurnoQuery(turnoUuid, tenantUuid);
        Result<TurnoResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isSuccess()).isTrue();
        TurnoResponseDto dto = resultado.getValue();
        assertThat(dto.id()).isEqualTo(turnoUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.unidadId()).isEqualTo(unidadUuid);
        assertThat(dto.choferId()).isEqualTo(choferUuid);
        assertThat(dto.distritoId()).isEqualTo(distritoUuid);
        assertThat(dto.fecha()).isEqualTo(fecha);
        assertThat(dto.horaInicio()).isEqualTo(inicio);
        assertThat(dto.horaFin()).isEqualTo(fin);
        assertThat(dto.tipoTurno()).isEqualTo("MANANA");
        assertThat(dto.estado()).isEqualTo("EN_CURSO");
    }

    @Test
    void obtener_turno_no_encontrado_retorna_error_not_found() {
        UUID tenantUuid = UUID.randomUUID();
        UUID turnoUuid = UUID.randomUUID();

        when(turnosPersistencePort.findById(any(TurnoId.class), any(TenantId.class)))
            .thenReturn(Optional.empty());

        ObtenerTurnoQuery query = new ObtenerTurnoQuery(turnoUuid, tenantUuid);
        Result<TurnoResponseDto> resultado = handler.obtener(query);

        assertThat(resultado.isFailure()).isTrue();
        assertThat(resultado.getError().code()).isEqualTo(ErrorCode.TURNO_NO_ENCONTRADO);
    }
}
