package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EstadoUnidadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarEstadosUnidadesQuery;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarEstadosUnidadesQueryHandlerTest {

    @Mock
    EstadoUnidadPersistencePort port;

    @InjectMocks
    ListarEstadosUnidadesQueryHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Camión compactador CAM-4892 - ruta matutina Miraflores
    private static final UUID UNIDAD_ID = UUID.randomUUID();

    private EstadoUnidadPersistencePort.EstadoUnidadView buildView() {
        return new EstadoUnidadPersistencePort.EstadoUnidadView(
                UUID.randomUUID(),
                TENANT_ID,
                UNIDAD_ID,
                null,
                // Coordenadas reales: Av. Larco, Miraflores, Lima
                -12.117880,
                -77.033043,
                // Velocidad típica en zona urbana de Lima (km/h)
                25.0,
                Instant.now(),
                "EN_MOVIMIENTO",
                Instant.now()
        );
    }

    @Test
    void listar_conEstados_retornaPaginaConDtos() {
        EstadoUnidadPersistencePort.EstadoUnidadView view = buildView();
        PageResult<EstadoUnidadPersistencePort.EstadoUnidadView> page = PageResult.of(List.of(view), 0, 20, 1L);
        when(port.findAllByTenant(any(TenantId.class), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<EstadoUnidadResponseDto>> result = handler.listar(
                new ListarEstadosUnidadesQuery(TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).unidadExternoId()).isEqualTo(UNIDAD_ID);
        assertThat(result.getValue().content().get(0).tenantId()).isEqualTo(TENANT_ID);
        assertThat(result.getValue().content().get(0).estadoMovimiento()).isEqualTo("EN_MOVIMIENTO");
    }

    @Test
    void listar_sinEstados_retornaPaginaVacia() {
        PageResult<EstadoUnidadPersistencePort.EstadoUnidadView> page = PageResult.empty(0, 20);
        when(port.findAllByTenant(any(TenantId.class), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<EstadoUnidadResponseDto>> result = handler.listar(
                new ListarEstadosUnidadesQuery(TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
