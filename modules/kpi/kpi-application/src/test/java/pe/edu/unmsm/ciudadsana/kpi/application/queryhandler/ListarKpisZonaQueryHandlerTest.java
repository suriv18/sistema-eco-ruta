package pe.edu.unmsm.ciudadsana.kpi.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiZonaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisZonaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarKpisZonaQueryHandlerTest {

    @Mock
    KpiPersistencePort kpiPersistencePort;

    @InjectMocks
    ListarKpisZonaQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ZONA_ID = UUID.randomUUID();
    private static final LocalDate DESDE = LocalDate.of(2026, 6, 1);
    private static final LocalDate HASTA = LocalDate.of(2026, 6, 29);

    private KpiZonaDto kpiZonaDto() {
        return new KpiZonaDto(UUID.randomUUID(), TENANT_ID, ZONA_ID, DESDE,
                4, 4, BigDecimal.valueOf(980.5), BigDecimal.valueOf(100.0), Instant.now());
    }

    @Test
    void listar_conKpis_retornaPagina() {
        PageResult<KpiZonaDto> page = PageResult.of(List.of(kpiZonaDto()), 0, 20, 1L);
        when(kpiPersistencePort.findKpisZona(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<KpiZonaDto>> result = handler.listar(
                new ListarKpisZonaQuery(TENANT_ID, ZONA_ID, DESDE, HASTA, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).zonaIdExterno()).isEqualTo(ZONA_ID);
    }

    @Test
    void listar_sinKpis_retornaPaginaVacia() {
        PageResult<KpiZonaDto> page = PageResult.empty(0, 20);
        when(kpiPersistencePort.findKpisZona(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<KpiZonaDto>> result = handler.listar(
                new ListarKpisZonaQuery(TENANT_ID, ZONA_ID, DESDE, HASTA, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
