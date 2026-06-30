package pe.edu.unmsm.ciudadsana.kpi.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiRutaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisRutaQuery;
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
class ListarKpisRutaQueryHandlerTest {

    @Mock
    KpiPersistencePort kpiPersistencePort;

    @InjectMocks
    ListarKpisRutaQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final LocalDate DESDE = LocalDate.of(2026, 6, 1);
    private static final LocalDate HASTA = LocalDate.of(2026, 6, 29);

    private KpiRutaDto kpiRutaDto() {
        return new KpiRutaDto(UUID.randomUUID(), TENANT_ID, UUID.randomUUID(), DESDE,
                BigDecimal.valueOf(15000), BigDecimal.valueOf(14800), 7200, 7100, 5, 5,
                BigDecimal.valueOf(98.5), BigDecimal.valueOf(0.12), Instant.now());
    }

    @Test
    void listar_conKpis_retornaPagina() {
        PageResult<KpiRutaDto> page = PageResult.of(List.of(kpiRutaDto()), 0, 20, 1L);
        when(kpiPersistencePort.findKpisRuta(any(), any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<KpiRutaDto>> result = handler.listar(
                new ListarKpisRutaQuery(TENANT_ID, DESDE, HASTA, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().totalElements()).isEqualTo(1L);
    }

    @Test
    void listar_sinKpis_retornaPaginaVacia() {
        PageResult<KpiRutaDto> page = PageResult.empty(0, 20);
        when(kpiPersistencePort.findKpisRuta(any(), any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<KpiRutaDto>> result = handler.listar(
                new ListarKpisRutaQuery(TENANT_ID, DESDE, HASTA, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
