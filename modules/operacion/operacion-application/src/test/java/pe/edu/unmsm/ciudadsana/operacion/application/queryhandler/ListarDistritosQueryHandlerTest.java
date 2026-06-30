package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarDistritosQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDistrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarDistritosQueryHandlerTest {

    @Mock
    private DistritosPersistencePort distritosPersistencePort;

    @InjectMocks
    private ListarDistritosQueryHandler handler;

    @Test
    void listar_con_elementos_retorna_page_result_con_dtos() {
        UUID tenantUuid = UUID.randomUUID();
        UUID distritoUuid = UUID.randomUUID();
        Instant ahora = Instant.now();

        Distrito distrito = Distrito.reconstitute(
            DistritoId.of(distritoUuid),
            TenantId.of(tenantUuid),
            "Miraflores",
            "150122",
            EstadoDistrito.ACTIVO,
            ahora
        );

        PageResult<Distrito> pageResult = PageResult.of(List.of(distrito), 0, 10, 1L);
        when(distritosPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageResult);

        ListarDistritosQuery query = new ListarDistritosQuery(tenantUuid, 0, 10);
        Result<PageResult<DistritoResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<DistritoResponseDto> page = resultado.getValue();
        assertThat(page.content()).hasSize(1);
        assertThat(page.totalElements()).isEqualTo(1L);
        DistritoResponseDto dto = page.content().getFirst();
        assertThat(dto.id()).isEqualTo(distritoUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.nombre()).isEqualTo("Miraflores");
        assertThat(dto.ubigeo()).isEqualTo("150122");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
    }

    @Test
    void listar_sin_elementos_retorna_page_result_vacio() {
        UUID tenantUuid = UUID.randomUUID();
        PageResult<Distrito> pageVacio = PageResult.empty(0, 10);
        when(distritosPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageVacio);

        ListarDistritosQuery query = new ListarDistritosQuery(tenantUuid, 0, 10);
        Result<PageResult<DistritoResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<DistritoResponseDto> page = resultado.getValue();
        assertThat(page.content()).isEmpty();
        assertThat(page.totalElements()).isZero();
    }
}
