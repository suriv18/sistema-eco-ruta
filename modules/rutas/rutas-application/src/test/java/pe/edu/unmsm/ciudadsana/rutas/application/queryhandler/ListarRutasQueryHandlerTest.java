package pe.edu.unmsm.ciudadsana.rutas.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ListarRutasQuery;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.EstadoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.enums.TipoRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DepositoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.RutaId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.TurnoExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarRutasQueryHandlerTest {

    @Mock
    RutasPersistencePort rutasPersistencePort;

    @InjectMocks
    ListarRutasQueryHandler handler;

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
        when(ruta.getTipoRuta()).thenReturn(TipoRuta.HISTORICA);
        when(ruta.getEstado()).thenReturn(EstadoRuta.BORRADOR);
        when(ruta.getMetricas()).thenReturn(MetricasRuta.of(0, 0, 0));
        when(ruta.getVersionActual()).thenReturn(Optional.empty());
        when(ruta.getCreadoEn()).thenReturn(Instant.now());
        when(ruta.getActualizadoEn()).thenReturn(Optional.empty());
        return ruta;
    }

    @Test
    void listar_conRutas_retornaPaginaConDtos() {
        PageResult<Ruta> page = PageResult.of(List.of(rutaMock()), 0, 20, 1L);
        when(rutasPersistencePort.findAllByTenantId(any(), isNull(), isNull(), isNull(), anyInt(), anyInt()))
                .thenReturn(page);

        Result<PageResult<RutaResponseDto>> result = handler.listar(
                new ListarRutasQuery(TENANT_ID, null, null, null, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).id()).isEqualTo(RUTA_ID);
        assertThat(result.getValue().content().get(0).estado()).isEqualTo("BORRADOR");
    }

    @Test
    void listar_sinRutas_retornaPaginaVacia() {
        PageResult<Ruta> page = PageResult.empty(0, 20);
        when(rutasPersistencePort.findAllByTenantId(any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(page);

        Result<PageResult<RutaResponseDto>> result = handler.listar(
                new ListarRutasQuery(TENANT_ID, null, null, null, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
