package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.PingGpsResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.PingGpsPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarPingsUnidadQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.OrigenPing;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.PingGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.PingId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarPingsUnidadQueryHandlerTest {

    @Mock
    PingGpsPersistencePort pingGpsPersistencePort;

    @InjectMocks
    ListarPingsUnidadQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNIDAD_ID = UUID.randomUUID();
    private static final UUID PING_ID = UUID.randomUUID();
    private static final UUID DISPOSITIVO_ID = UUID.randomUUID();

    private PingGps pingMock() {
        PingGps ping = mock(PingGps.class);
        when(ping.getId()).thenReturn(PingId.of(PING_ID));
        when(ping.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(ping.getDispositivoId()).thenReturn(DispositivoId.of(DISPOSITIVO_ID));
        when(ping.getUnidadExternoId()).thenReturn(UnidadExternoId.of(UNIDAD_ID));
        when(ping.getRutaExternoId()).thenReturn(Optional.empty());
        when(ping.getTs()).thenReturn(Instant.now());
        when(ping.getPosicion()).thenReturn(Coordenadas.of(-12.046, -77.042));
        when(ping.getVelocidadKmh()).thenReturn(Optional.of(35.5));
        when(ping.getRumboGrados()).thenReturn(Optional.empty());
        when(ping.getPrecisionM()).thenReturn(Optional.empty());
        when(ping.getOrigen()).thenReturn(OrigenPing.GPS_REAL);
        when(ping.getRecibidoEn()).thenReturn(Instant.now());
        return ping;
    }

    @Test
    void listar_conPings_retornaPaginaConDtos() {
        PageResult<PingGps> page = PageResult.of(List.of(pingMock()), 0, 20, 1L);
        when(pingGpsPersistencePort.findAllByUnidad(any(UnidadExternoId.class), any(TenantId.class), anyInt(), anyInt()))
                .thenReturn(page);

        Result<PageResult<PingGpsResponseDto>> result = handler.listar(
                new ListarPingsUnidadQuery(UNIDAD_ID, TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).id()).isEqualTo(PING_ID);
        assertThat(result.getValue().content().get(0).unidadExternoId()).isEqualTo(UNIDAD_ID);
        assertThat(result.getValue().content().get(0).origen()).isEqualTo("GPS_REAL");
    }

    @Test
    void listar_sinPings_retornaPaginaVacia() {
        PageResult<PingGps> page = PageResult.empty(0, 20);
        when(pingGpsPersistencePort.findAllByUnidad(any(UnidadExternoId.class), any(TenantId.class), anyInt(), anyInt()))
                .thenReturn(page);

        Result<PageResult<PingGpsResponseDto>> result = handler.listar(
                new ListarPingsUnidadQuery(UNIDAD_ID, TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
