package pe.edu.unmsm.ciudadsana.rutas.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaDetalleResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ObtenerRutaDetalleQuery;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerRutaDetalleQueryHandlerTest {

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @InjectMocks
    ObtenerRutaDetalleQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID RUTA_ID = UUID.randomUUID();

    private Ruta rutaMock() {
        UUID turnoId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        UUID depositoId = UUID.randomUUID();

        Ruta ruta = mock(Ruta.class);
        when(ruta.getId()).thenReturn(RutaId.of(RUTA_ID));
        when(ruta.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(ruta.getTurnoId()).thenReturn(TurnoExternoId.of(turnoId));
        when(ruta.getDistritoId()).thenReturn(DistritoExternoId.of(distritoId));
        when(ruta.getDepositoOrigenId()).thenReturn(DepositoExternoId.of(depositoId));
        when(ruta.getDepositoDestinoId()).thenReturn(DepositoExternoId.of(depositoId));
        when(ruta.getFecha()).thenReturn(LocalDate.of(2026, 6, 29));
        when(ruta.getTipoRuta()).thenReturn(TipoRuta.OPTIMIZADA);
        when(ruta.getEstado()).thenReturn(EstadoRuta.APROBADA);
        when(ruta.getMetricas()).thenReturn(MetricasRuta.of(12500, 3600, 8.5));
        when(ruta.getVersionActual()).thenReturn(Optional.empty());
        when(ruta.getHistorialVersiones()).thenReturn(List.of());
        when(ruta.getEventos()).thenReturn(List.of());
        when(ruta.getCreadoEn()).thenReturn(Instant.now());
        when(ruta.getActualizadoEn()).thenReturn(Optional.empty());
        return ruta;
    }

    @Test
    void obtenerDetalle_rutaExistente_retornaDetalleConHistorial() {
        Ruta ruta = rutaMock();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));

        Result<RutaDetalleResponseDto> result = handler.obtenerDetalle(
                new ObtenerRutaDetalleQuery(RUTA_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(RUTA_ID);
        assertThat(result.getValue().estado()).isEqualTo("APROBADA");
        assertThat(result.getValue().tipoRuta()).isEqualTo("OPTIMIZADA");
        assertThat(result.getValue().historialVersiones()).isEmpty();
        assertThat(result.getValue().eventos()).isEmpty();
    }

    @Test
    void obtenerDetalle_rutaNoExiste_retornaNoEncontrada() {
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        Result<RutaDetalleResponseDto> result = handler.obtenerDetalle(
                new ObtenerRutaDetalleQuery(RUTA_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_NO_ENCONTRADA);
    }
}
