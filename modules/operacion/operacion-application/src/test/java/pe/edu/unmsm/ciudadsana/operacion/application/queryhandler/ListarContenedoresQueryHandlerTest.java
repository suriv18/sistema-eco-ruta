package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ContenedoresPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarContenedoresQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoContenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Contenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadM3;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
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
class ListarContenedoresQueryHandlerTest {

    @Mock
    private ContenedoresPersistencePort contenedoresPersistencePort;

    @InjectMocks
    private ListarContenedoresQueryHandler handler;

    @Test
    void listar_con_elementos_retorna_page_result_con_dtos() {
        UUID tenantUuid = UUID.randomUUID();
        UUID contenedorUuid = UUID.randomUUID();
        UUID zonaUuid = UUID.randomUUID();
        Instant ahora = Instant.now();

        Contenedor contenedor = mock(Contenedor.class);
        CapacidadM3 capacidad = CapacidadM3.of(new BigDecimal("5.0"));

        when(contenedor.getId()).thenReturn(ContenedorId.of(contenedorUuid));
        when(contenedor.getTenantId()).thenReturn(TenantId.of(tenantUuid));
        when(contenedor.getZonaId()).thenReturn(ZonaId.of(zonaUuid));
        when(contenedor.getCodigo()).thenReturn("CONT-001");
        when(contenedor.getCapacidad()).thenReturn(capacidad);
        when(contenedor.getEstadoContenedor()).thenReturn(EstadoContenedor.VACIO);
        when(contenedor.getCreadoEn()).thenReturn(ahora);

        PageResult<Contenedor> pageResult = PageResult.of(List.of(contenedor), 0, 10, 1L);
        when(contenedoresPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageResult);

        ListarContenedoresQuery query = new ListarContenedoresQuery(tenantUuid, 0, 10);
        Result<PageResult<ContenedorResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<ContenedorResponseDto> page = resultado.getValue();
        assertThat(page.content()).hasSize(1);
        assertThat(page.totalElements()).isEqualTo(1L);
        ContenedorResponseDto dto = page.content().getFirst();
        assertThat(dto.id()).isEqualTo(contenedorUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.zonaId()).isEqualTo(zonaUuid);
        assertThat(dto.codigo()).isEqualTo("CONT-001");
        assertThat(dto.capacidadM3()).isEqualByComparingTo(new BigDecimal("5.0"));
        assertThat(dto.estadoContenedor()).isEqualTo("VACIO");
    }

    @Test
    void listar_sin_elementos_retorna_page_result_vacio() {
        UUID tenantUuid = UUID.randomUUID();
        PageResult<Contenedor> pageVacio = PageResult.empty(0, 10);
        when(contenedoresPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageVacio);

        ListarContenedoresQuery query = new ListarContenedoresQuery(tenantUuid, 0, 10);
        Result<PageResult<ContenedorResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<ContenedorResponseDto> page = resultado.getValue();
        assertThat(page.content()).isEmpty();
        assertThat(page.totalElements()).isZero();
    }
}
