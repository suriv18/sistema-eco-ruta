package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DepositosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarDepositosQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Deposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarDepositosQueryHandlerTest {

    @Mock
    private DepositosPersistencePort depositosPersistencePort;

    @InjectMocks
    private ListarDepositosQueryHandler handler;

    @Test
    void listar_con_elementos_retorna_page_result_con_dtos() {
        UUID tenantUuid = UUID.randomUUID();
        UUID depositoUuid = UUID.randomUUID();
        UUID distritoUuid = UUID.randomUUID();
        Instant ahora = Instant.now();

        Deposito deposito = Deposito.reconstitute(
            DepositoId.of(depositoUuid),
            TenantId.of(tenantUuid),
            DistritoId.of(distritoUuid),
            "Deposito Norte",
            TipoDeposito.BASE,
            EstadoDeposito.ACTIVO,
            ahora
        );

        when(depositosPersistencePort.countByTenantId(any(TenantId.class))).thenReturn(1L);
        when(depositosPersistencePort.findAllByTenantId(any(TenantId.class))).thenReturn(List.of(deposito));

        ListarDepositosQuery query = new ListarDepositosQuery(tenantUuid, 0, 10);
        Result<PageResult<DepositoResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<DepositoResponseDto> page = resultado.getValue();
        assertThat(page.content()).hasSize(1);
        assertThat(page.totalElements()).isEqualTo(1L);
        DepositoResponseDto dto = page.content().getFirst();
        assertThat(dto.id()).isEqualTo(depositoUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.distritoId()).isEqualTo(distritoUuid);
        assertThat(dto.nombre()).isEqualTo("Deposito Norte");
        assertThat(dto.tipo()).isEqualTo("BASE");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
    }

    @Test
    void listar_sin_elementos_retorna_page_result_vacio() {
        UUID tenantUuid = UUID.randomUUID();
        when(depositosPersistencePort.countByTenantId(any(TenantId.class))).thenReturn(0L);
        when(depositosPersistencePort.findAllByTenantId(any(TenantId.class))).thenReturn(List.of());

        ListarDepositosQuery query = new ListarDepositosQuery(tenantUuid, 0, 10);
        Result<PageResult<DepositoResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<DepositoResponseDto> page = resultado.getValue();
        assertThat(page.content()).isEmpty();
        assertThat(page.totalElements()).isZero();
    }
}
