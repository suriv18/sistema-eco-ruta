package pe.edu.unmsm.ciudadsana.kpi.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.ResumenOperativoDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ObtenerResumenDiarioQuery;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerResumenDiarioQueryHandlerTest {

    @Mock
    KpiPersistencePort kpiPersistencePort;

    @InjectMocks
    ObtenerResumenDiarioQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISTRITO_ID = UUID.randomUUID();
    private static final LocalDate FECHA = LocalDate.of(2026, 6, 29);

    private ResumenOperativoDto resumenDto() {
        return new ResumenOperativoDto(
                UUID.randomUUID(), TENANT_ID, DISTRITO_ID, FECHA,
                BigDecimal.valueOf(120.5), BigDecimal.valueOf(118.0),
                BigDecimal.valueOf(45.2), BigDecimal.valueOf(97.9),
                15, 14, BigDecimal.valueOf(18.3), Instant.now());
    }

    @Test
    void obtener_resumenExistente_retornaDto() {
        ResumenOperativoDto dto = resumenDto();
        when(kpiPersistencePort.findResumenByDistritoAndFecha(any(), any(), any()))
                .thenReturn(Optional.of(dto));

        Result<ResumenOperativoDto> result = handler.obtener(
                new ObtenerResumenDiarioQuery(TENANT_ID, DISTRITO_ID, FECHA));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().tenantId()).isEqualTo(TENANT_ID);
        assertThat(result.getValue().distritoIdExterno()).isEqualTo(DISTRITO_ID);
        assertThat(result.getValue().fecha()).isEqualTo(FECHA);
    }

    @Test
    void obtener_sinResumen_retornaRecursoNoEncontrado() {
        when(kpiPersistencePort.findResumenByDistritoAndFecha(any(), any(), any()))
                .thenReturn(Optional.empty());

        Result<ResumenOperativoDto> result = handler.obtener(
                new ObtenerResumenDiarioQuery(TENANT_ID, DISTRITO_ID, FECHA));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RECURSO_NO_ENCONTRADO);
    }
}
