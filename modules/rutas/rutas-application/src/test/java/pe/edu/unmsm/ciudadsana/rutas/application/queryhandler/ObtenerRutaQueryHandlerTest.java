package pe.edu.unmsm.ciudadsana.rutas.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ObtenerRutaQuery;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerRutaQueryHandlerTest {

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @InjectMocks
    ObtenerRutaQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID RUTA_ID = UUID.randomUUID();

    @Test
    void obtener_rutaNoExiste_retornaNoEncontrada() {
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.empty());

        Result<RutaResponseDto> result = handler.obtener(new ObtenerRutaQuery(RUTA_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RUTA_NO_ENCONTRADA);
    }

    @Test
    void obtener_rutaExistente_retornaDto() {
        Ruta ruta = rutaMock();
        when(rutasPersistencePort.findByIdAndTenantId(any(RutaId.class), any(TenantId.class)))
                .thenReturn(Optional.of(ruta));

        Result<RutaResponseDto> result = handler.obtener(new ObtenerRutaQuery(RUTA_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(RUTA_ID);
        assertThat(result.getValue().tenantId()).isEqualTo(TENANT_ID);
    }

    private Ruta rutaMock() {
        Ruta ruta = mock(Ruta.class);
        UUID turnoId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        UUID depositoId = UUID.randomUUID();
        java.time.LocalDate fecha = java.time.LocalDate.of(2026, 6, 29);

        when(ruta.getId()).thenReturn(RutaId.of(RUTA_ID));
        when(ruta.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(ruta.getTurnoId()).thenReturn(pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.TurnoExternoId.of(turnoId));
        when(ruta.getDistritoId()).thenReturn(pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DistritoExternoId.of(distritoId));
        when(ruta.getDepositoOrigenId()).thenReturn(pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DepositoExternoId.of(depositoId));
        when(ruta.getDepositoDestinoId()).thenReturn(pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DepositoExternoId.of(depositoId));
        when(ruta.getFecha()).thenReturn(fecha);
        when(ruta.getTipoRuta()).thenReturn(pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoRuta.HISTORICA);
        when(ruta.getEstado()).thenReturn(pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoRuta.BORRADOR);
        when(ruta.getMetricas()).thenReturn(pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta.of(0, 0, 0));
        when(ruta.getVersionActual()).thenReturn(Optional.empty());
        when(ruta.getCreadoEn()).thenReturn(java.time.Instant.now());
        when(ruta.getActualizadoEn()).thenReturn(Optional.empty());
        return ruta;
    }
}
