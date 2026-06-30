package pe.edu.unmsm.ciudadsana.kpi.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiUnidadDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisUnidadQuery;
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
class ListarKpisUnidadQueryHandlerTest {

    @Mock
    KpiPersistencePort kpiPersistencePort;

    @InjectMocks
    ListarKpisUnidadQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNIDAD_ID = UUID.randomUUID();
    private static final LocalDate DESDE = LocalDate.of(2026, 6, 1);
    private static final LocalDate HASTA = LocalDate.of(2026, 6, 29);

    private KpiUnidadDto kpiUnidadDto() {
        return new KpiUnidadDto(UUID.randomUUID(), TENANT_ID, UNIDAD_ID, DESDE,
                BigDecimal.valueOf(85.3), BigDecimal.valueOf(8.5),
                BigDecimal.valueOf(12.4), BigDecimal.valueOf(30.2), Instant.now());
    }

    @Test
    void listar_conKpis_retornaPagina() {
        PageResult<KpiUnidadDto> page = PageResult.of(List.of(kpiUnidadDto()), 0, 20, 1L);
        when(kpiPersistencePort.findKpisUnidad(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<KpiUnidadDto>> result = handler.listar(
                new ListarKpisUnidadQuery(TENANT_ID, UNIDAD_ID, DESDE, HASTA, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).unidadIdExterno()).isEqualTo(UNIDAD_ID);
    }

    @Test
    void listar_sinKpis_retornaPaginaVacia() {
        PageResult<KpiUnidadDto> page = PageResult.empty(0, 20);
        when(kpiPersistencePort.findKpisUnidad(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<KpiUnidadDto>> result = handler.listar(
                new ListarKpisUnidadQuery(TENANT_ID, UNIDAD_ID, DESDE, HASTA, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
