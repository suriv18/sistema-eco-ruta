package pe.edu.unmsm.ciudadsana.kpi.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.kpi.application.command.CalcularResumenDiarioCommand;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.ResumenOperativoDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
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
class CalcularResumenDiarioCommandHandlerTest {

    @Mock
    KpiPersistencePort kpiPersistencePort;

    @InjectMocks
    CalcularResumenDiarioCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISTRITO_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final LocalDate FECHA = LocalDate.of(2026, 6, 29);

    private ResumenOperativoDto resumenExistente() {
        return new ResumenOperativoDto(
                UUID.randomUUID(), TENANT_ID, DISTRITO_ID, FECHA,
                BigDecimal.valueOf(100.0), BigDecimal.valueOf(95.0),
                BigDecimal.valueOf(30.0), BigDecimal.valueOf(95.0),
                10, 9, BigDecimal.valueOf(12.5), Instant.now()
        );
    }

    @Test
    void calcular_resumenExistente_retornaExistente() {
        ResumenOperativoDto existente = resumenExistente();
        when(kpiPersistencePort.findResumenByDistritoAndFecha(TENANT_ID, DISTRITO_ID, FECHA))
                .thenReturn(Optional.of(existente));
        when(kpiPersistencePort.saveResumen(any())).thenAnswer(inv -> inv.getArgument(0));

        Result<ResumenOperativoDto> result = handler.calcular(
                new CalcularResumenDiarioCommand(TENANT_ID, DISTRITO_ID, FECHA));

        assertThat(result.isSuccess()).isTrue();
        ResumenOperativoDto dto = result.getValue();
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.distritoIdExterno()).isEqualTo(DISTRITO_ID);
        assertThat(dto.fecha()).isEqualTo(FECHA);
        assertThat(dto.kmProgramados()).isEqualByComparingTo(BigDecimal.valueOf(100.0));
        assertThat(dto.kmRecorridos()).isEqualByComparingTo(BigDecimal.valueOf(95.0));
    }

    @Test
    void calcular_resumenNuevo_creaYRetorna() {
        when(kpiPersistencePort.findResumenByDistritoAndFecha(TENANT_ID, DISTRITO_ID, FECHA))
                .thenReturn(Optional.empty());
        when(kpiPersistencePort.saveResumen(any())).thenAnswer(inv -> inv.getArgument(0));

        Result<ResumenOperativoDto> result = handler.calcular(
                new CalcularResumenDiarioCommand(TENANT_ID, DISTRITO_ID, FECHA));

        assertThat(result.isSuccess()).isTrue();
        ResumenOperativoDto dto = result.getValue();
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.distritoIdExterno()).isEqualTo(DISTRITO_ID);
        assertThat(dto.fecha()).isEqualTo(FECHA);
        assertThat(dto.kmProgramados()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(dto.kmRecorridos()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(dto.toneladasRecolectadas()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(dto.alertasRegistradas()).isZero();
        assertThat(dto.resumenId()).isNotNull();
    }
}
