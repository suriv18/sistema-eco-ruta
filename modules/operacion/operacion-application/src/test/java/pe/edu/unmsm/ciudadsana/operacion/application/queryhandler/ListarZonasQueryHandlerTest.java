package pe.edu.unmsm.ciudadsana.operacion.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarZonasQuery;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CodigoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.PrioridadBase;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarZonasQueryHandlerTest {

    @Mock
    private ZonasPersistencePort zonasPersistencePort;

    @InjectMocks
    private ListarZonasQueryHandler handler;

    @Test
    void listar_con_elementos_retorna_page_result_con_dtos() {
        UUID tenantUuid = UUID.randomUUID();
        UUID zonaUuid = UUID.randomUUID();
        UUID distritoUuid = UUID.randomUUID();
        Instant ahora = Instant.now();

        Zona zona = mock(Zona.class);
        CodigoZona codigo = CodigoZona.of("ZN-01");
        PrioridadBase prioridad = PrioridadBase.of(1);

        when(zona.getId()).thenReturn(ZonaId.of(zonaUuid));
        when(zona.getTenantId()).thenReturn(TenantId.of(tenantUuid));
        when(zona.getDistritoId()).thenReturn(DistritoId.of(distritoUuid));
        when(zona.getCodigo()).thenReturn(codigo);
        when(zona.getNombre()).thenReturn("Zona Norte");
        when(zona.getTipo()).thenReturn(TipoZona.RESIDENCIAL);
        when(zona.getPrioridad()).thenReturn(prioridad);
        when(zona.getEstado()).thenReturn(EstadoZona.ACTIVA);
        when(zona.getCreadoEn()).thenReturn(ahora);

        PageResult<Zona> pageResult = PageResult.of(List.of(zona), 0, 10, 1L);
        when(zonasPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageResult);

        ListarZonasQuery query = new ListarZonasQuery(tenantUuid, 0, 10);
        Result<PageResult<ZonaResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<ZonaResponseDto> page = resultado.getValue();
        assertThat(page.content()).hasSize(1);
        assertThat(page.totalElements()).isEqualTo(1L);
        ZonaResponseDto dto = page.content().getFirst();
        assertThat(dto.id()).isEqualTo(zonaUuid);
        assertThat(dto.tenantId()).isEqualTo(tenantUuid);
        assertThat(dto.distritoId()).isEqualTo(distritoUuid);
        assertThat(dto.codigo()).isEqualTo("ZN-01");
        assertThat(dto.nombre()).isEqualTo("Zona Norte");
        assertThat(dto.tipoZona()).isEqualTo("RESIDENCIAL");
        assertThat(dto.prioridad()).isEqualTo(1);
        assertThat(dto.estado()).isEqualTo("ACTIVA");
    }

    @Test
    void listar_sin_elementos_retorna_page_result_vacio() {
        UUID tenantUuid = UUID.randomUUID();
        PageResult<Zona> pageVacio = PageResult.empty(0, 10);
        when(zonasPersistencePort.findAll(any(TenantId.class), eq(0), eq(10))).thenReturn(pageVacio);

        ListarZonasQuery query = new ListarZonasQuery(tenantUuid, 0, 10);
        Result<PageResult<ZonaResponseDto>> resultado = handler.listar(query);

        assertThat(resultado.isSuccess()).isTrue();
        PageResult<ZonaResponseDto> page = resultado.getValue();
        assertThat(page.content()).isEmpty();
        assertThat(page.totalElements()).isZero();
    }
}
