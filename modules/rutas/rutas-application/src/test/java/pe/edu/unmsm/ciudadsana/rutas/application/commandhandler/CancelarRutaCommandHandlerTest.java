package pe.edu.unmsm.ciudadsana.rutas.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pe.edu.unmsm.ciudadsana.rutas.application.command.CancelarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasEventPublisherPort;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DepositoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.TurnoExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CancelarRutaCommandHandlerTest {

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @Mock
    RutasEventPublisherPort eventPublisher;

    @InjectMocks
    CancelarRutaCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID RUTA_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void cancelar_rutaNoExiste_retornaRutaNoEncontrada() {
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        Result<RutaResponseDto> result = handler.cancelar(new CancelarRutaCommand(RUTA_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_NO_ENCONTRADA);
    }

    @Test
    void cancelar_rutaEnBorrador_cancelaConExito() {
        Ruta ruta = rutaMock(EstadoRuta.CANCELADA);
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        when(rutasPersistencePort.save(ruta)).thenReturn(ruta);
        when(ruta.pullDomainEvents()).thenReturn(List.of());

        Result<RutaResponseDto> result = handler.cancelar(new CancelarRutaCommand(RUTA_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(RUTA_ID);
        verify(ruta).cancelar();
        verify(rutasPersistencePort).save(ruta);
        verify(eventPublisher).publishAll(any());
    }

    @Test
    void cancelar_transicionInvalida_retornaTransicionEstadoInvalida() {
        Ruta ruta = rutaMock(EstadoRuta.EN_EJECUCION);
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));
        doThrow(new IllegalStateException("Estado inválido")).when(ruta).cancelar();

        Result<RutaResponseDto> result = handler.cancelar(new CancelarRutaCommand(RUTA_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.TRANSICION_ESTADO_INVALIDA);
    }

    private Ruta rutaMock(EstadoRuta estado) {
        Ruta ruta = mock(Ruta.class);
        UUID turnoId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        UUID depositoId = UUID.randomUUID();
        LocalDate fecha = LocalDate.of(2026, 6, 29);

        when(ruta.getId()).thenReturn(RutaId.of(RUTA_ID));
        when(ruta.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(ruta.getTurnoId()).thenReturn(TurnoExternoId.of(turnoId));
        when(ruta.getDistritoId()).thenReturn(DistritoExternoId.of(distritoId));
        when(ruta.getDepositoOrigenId()).thenReturn(DepositoExternoId.of(depositoId));
        when(ruta.getDepositoDestinoId()).thenReturn(DepositoExternoId.of(depositoId));
        when(ruta.getFecha()).thenReturn(fecha);
        when(ruta.getTipoRuta()).thenReturn(TipoRuta.HISTORICA);
        when(ruta.getEstado()).thenReturn(estado);
        when(ruta.getMetricas()).thenReturn(MetricasRuta.of(0, 0, 0));
        when(ruta.getVersionActual()).thenReturn(Optional.empty());
        when(ruta.getCreadoEn()).thenReturn(Instant.now());
        when(ruta.getActualizadoEn()).thenReturn(Optional.empty());
        return ruta;
    }
}
