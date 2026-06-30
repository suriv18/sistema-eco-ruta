package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CrearTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearTurnoCommandHandlerTest {

    @Mock
    UnidadesPersistencePort unidadesPersistencePort;

    @Mock
    ChoferesPersistencePort choferesPersistencePort;

    @Mock
    DistritosPersistencePort distritosPersistencePort;

    @Mock
    TurnosPersistencePort turnosPersistencePort;

    @Mock
    OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    CrearTurnoCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNIDAD_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID CHOFER_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final UUID DISTRITO_ID = UUID.fromString("55555555-5555-5555-5555-555555555555");
    private static final LocalDate FECHA = LocalDate.of(2026, 7, 1);
    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(16, 0);

    private CrearTurnoCommand comandoValido() {
        return new CrearTurnoCommand(TENANT_ID, UNIDAD_ID, CHOFER_ID, DISTRITO_ID, FECHA, HORA_INICIO, HORA_FIN, "MANANA");
    }

    private Turno turnoGuardadoMock() {
        Turno turno = mock(Turno.class);
        when(turno.getId()).thenReturn(TurnoId.of(UUID.randomUUID()));
        when(turno.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(turno.getUnidadId()).thenReturn(UnidadId.of(UNIDAD_ID));
        when(turno.getChoferId()).thenReturn(ChoferId.of(CHOFER_ID));
        when(turno.getDistritoId()).thenReturn(DistritoId.of(DISTRITO_ID));
        when(turno.getFecha()).thenReturn(FECHA);
        when(turno.getHoraInicio()).thenReturn(HORA_INICIO);
        when(turno.getHoraFin()).thenReturn(HORA_FIN);
        when(turno.getTipo()).thenReturn(TipoTurno.MANANA);
        when(turno.getEstado()).thenReturn(EstadoTurno.PROGRAMADO);
        when(turno.getCreadoEn()).thenReturn(Instant.now());
        when(turno.pullDomainEvents()).thenReturn(List.of());
        return turno;
    }

    @Test
    void crear_turno_exitoso_retorna_turno_dto() {
        Unidad unidad = mock(Unidad.class);
        when(unidad.estaDisponible()).thenReturn(true);
        Chofer chofer = mock(Chofer.class);
        when(chofer.estaDisponible()).thenReturn(true);

        when(unidadesPersistencePort.findById(UnidadId.of(UNIDAD_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(unidad));
        when(choferesPersistencePort.findById(ChoferId.of(CHOFER_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(chofer));
        when(turnosPersistencePort.existeSuperposicionUnidad(any(), any(), any(), any(), any())).thenReturn(false);
        when(turnosPersistencePort.existeSuperposicionChofer(any(), any(), any(), any(), any())).thenReturn(false);

        Turno turno = turnoGuardadoMock();
        when(turnosPersistencePort.save(any())).thenReturn(turno);

        Result<TurnoResponseDto> result = handler.crear(comandoValido());

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().tipoTurno()).isEqualTo("MANANA");
        assertThat(result.getValue().estado()).isEqualTo("PROGRAMADO");
        verify(turnosPersistencePort).save(any());
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void crear_turno_unidad_no_encontrada_retorna_failure() {
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        Result<TurnoResponseDto> result = handler.crear(comandoValido());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.UNIDAD_NO_ENCONTRADA);
        verify(turnosPersistencePort, never()).save(any());
    }

    @Test
    void crear_turno_unidad_no_disponible_retorna_failure() {
        Unidad unidad = mock(Unidad.class);
        when(unidad.estaDisponible()).thenReturn(false);
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.of(unidad));

        Result<TurnoResponseDto> result = handler.crear(comandoValido());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.UNIDAD_NO_DISPONIBLE);
        verify(turnosPersistencePort, never()).save(any());
    }

    @Test
    void crear_turno_chofer_no_encontrado_retorna_failure() {
        Unidad unidad = mock(Unidad.class);
        when(unidad.estaDisponible()).thenReturn(true);
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.of(unidad));
        when(choferesPersistencePort.findById(any(), any())).thenReturn(Optional.empty());

        Result<TurnoResponseDto> result = handler.crear(comandoValido());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.CHOFER_NO_ENCONTRADO);
        verify(turnosPersistencePort, never()).save(any());
    }

    @Test
    void crear_turno_chofer_no_disponible_retorna_failure() {
        Unidad unidad = mock(Unidad.class);
        when(unidad.estaDisponible()).thenReturn(true);
        Chofer chofer = mock(Chofer.class);
        when(chofer.estaDisponible()).thenReturn(false);
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.of(unidad));
        when(choferesPersistencePort.findById(any(), any())).thenReturn(Optional.of(chofer));

        Result<TurnoResponseDto> result = handler.crear(comandoValido());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.CHOFER_NO_DISPONIBLE);
        verify(turnosPersistencePort, never()).save(any());
    }

    @Test
    void crear_turno_tipo_invalido_retorna_validacion_error() {
        Unidad unidad = mock(Unidad.class);
        when(unidad.estaDisponible()).thenReturn(true);
        Chofer chofer = mock(Chofer.class);
        when(chofer.estaDisponible()).thenReturn(true);
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.of(unidad));
        when(choferesPersistencePort.findById(any(), any())).thenReturn(Optional.of(chofer));

        CrearTurnoCommand command = new CrearTurnoCommand(
                TENANT_ID, UNIDAD_ID, CHOFER_ID, DISTRITO_ID, FECHA, HORA_INICIO, HORA_FIN, "TIPO_INVALIDO");

        Result<TurnoResponseDto> result = handler.crear(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
        verify(turnosPersistencePort, never()).save(any());
    }

    @Test
    void crear_turno_superposicion_unidad_retorna_failure() {
        Unidad unidad = mock(Unidad.class);
        when(unidad.estaDisponible()).thenReturn(true);
        Chofer chofer = mock(Chofer.class);
        when(chofer.estaDisponible()).thenReturn(true);
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.of(unidad));
        when(choferesPersistencePort.findById(any(), any())).thenReturn(Optional.of(chofer));
        when(turnosPersistencePort.existeSuperposicionUnidad(
                eq(UnidadId.of(UNIDAD_ID)), eq(FECHA), eq(HORA_INICIO), eq(HORA_FIN), any()))
                .thenReturn(true);

        Result<TurnoResponseDto> result = handler.crear(comandoValido());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.TURNO_SUPERPUESTO);
        verify(turnosPersistencePort, never()).save(any());
    }

    @Test
    void crear_turno_superposicion_chofer_retorna_failure() {
        Unidad unidad = mock(Unidad.class);
        when(unidad.estaDisponible()).thenReturn(true);
        Chofer chofer = mock(Chofer.class);
        when(chofer.estaDisponible()).thenReturn(true);
        when(unidadesPersistencePort.findById(any(), any())).thenReturn(Optional.of(unidad));
        when(choferesPersistencePort.findById(any(), any())).thenReturn(Optional.of(chofer));
        when(turnosPersistencePort.existeSuperposicionUnidad(any(), any(), any(), any(), any())).thenReturn(false);
        when(turnosPersistencePort.existeSuperposicionChofer(
                eq(ChoferId.of(CHOFER_ID)), eq(FECHA), eq(HORA_INICIO), eq(HORA_FIN), any()))
                .thenReturn(true);

        Result<TurnoResponseDto> result = handler.crear(comandoValido());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.TURNO_SUPERPUESTO);
        verify(turnosPersistencePort, never()).save(any());
    }
}
