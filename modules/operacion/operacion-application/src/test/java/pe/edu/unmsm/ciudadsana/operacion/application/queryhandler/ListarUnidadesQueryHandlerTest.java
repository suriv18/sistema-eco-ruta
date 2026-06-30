package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarUnidadesQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoOperativoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadKg;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadM3;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.Placa;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarUnidadesQueryHandlerTest {

    @Mock
    private UnidadesPersistencePort unidadesPersistencePort;

    @InjectMocks
    private ListarUnidadesQueryHandler handler;

    @Test
    void listar_con_elementos_retorna_page_result_con_dtos() {
        UUID tenantUuid = UUID.randomUUID();
        UUID unidadUuid = UUID.randomUUID();
        Instant ahora = Instant.now();

        Unidad unidad = mock(Unidad.class);
        Placa placa = Placa.of("ABC-123");
        CapacidadM3 capacidadM3 = CapacidadM3.of(new BigDecimal("8.0"));
        CapacidadKg capacidadKg = CapacidadKg.of(new BigDecimal("3000.0"));

        when(unidad.getId()).thenReturn(UnidadId.of(unidadUuid));
        when(unidad.getTenantId()).thenReturn(TenantId.of(tenantUuid));
        when(unidad.getPlaca()).thenReturn(placa);
        when(unidad.getCodigoInterno()).thenReturn("U-001");
        when(unidad.getTipo()).thenReturn(TipoUnidad.COMPACTADOR);
        when(unidad.getCapacidadM3()).thenReturn(capacidadM3);
        when(unidad.getCapacidadKg()).thenReturn(capacidadKg);
        when(unidad.getEstadoOperativo()).thenReturn(EstadoOperativoUnidad.OPERATIVA);
        when(unidad.getCreadoEn()).thenReturn(ahora);

        PageResult<Unidad> pageResult = PageResult.of(List.of(unidad), 0, 10, 1L);
        when(unidadesPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageResult);

        ListarUnidadesQuery query = new ListarUnidadesQuery(tenantUuid, 0, 10);
        Result<PageResult<UnidadResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<UnidadResponseDto> page = resultado.getValue();
        assertThat(page.content()).hasSize(1);
        assertThat(page.totalElements()).isEqualTo(1L);
        UnidadResponseDto dto = page.content().getFirst();
        assertThat(dto.id()).isEqualTo(unidadUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.placa()).isEqualTo("ABC-123");
        assertThat(dto.codigoInterno()).isEqualTo("U-001");
        assertThat(dto.tipoUnidad()).isEqualTo("COMPACTADOR");
        assertThat(dto.capacidadM3()).isEqualByComparingTo(new BigDecimal("8.0"));
        assertThat(dto.capacidadKg()).isEqualByComparingTo(new BigDecimal("3000.0"));
        assertThat(dto.estadoOperativo()).isEqualTo("OPERATIVA");
    }

    @Test
    void listar_sin_elementos_retorna_page_result_vacio() {
        UUID tenantUuid = UUID.randomUUID();
        PageResult<Unidad> pageVacio = PageResult.empty(0, 10);
        when(unidadesPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageVacio);

        ListarUnidadesQuery query = new ListarUnidadesQuery(tenantUuid, 0, 10);
        Result<PageResult<UnidadResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<UnidadResponseDto> page = resultado.getValue();
        assertThat(page.content()).isEmpty();
        assertThat(page.totalElements()).isZero();
    }
}
