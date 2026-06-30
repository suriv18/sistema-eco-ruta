package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DesvioRutaResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort.DesvioView;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarDesviosActivosQuery;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarDesviosActivosQueryHandlerTest {

    @Mock
    DesvioRutaPersistencePort desvioRutaPersistencePort;

    @InjectMocks
    ListarDesviosActivosQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID RUTA_ID = UUID.randomUUID();

    private DesvioView desvioView() {
        return new DesvioView(UUID.randomUUID(), TENANT_ID, UUID.randomUUID(), RUTA_ID,
                -12.05, -77.05, 150.0, "MEDIA", "ABIERTO", Instant.now(), null);
    }

    @Test
    void listar_conDesvios_retornaPaginaConDtos() {
        PageResult<DesvioView> page = PageResult.of(List.of(desvioView()), 0, 20, 1L);
        when(desvioRutaPersistencePort.findActivosByRuta(any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<DesvioRutaResponseDto>> result = handler.listar(
                new ListarDesviosActivosQuery(RUTA_ID, TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(1);
        assertThat(result.getValue().content().get(0).rutaExternoId()).isEqualTo(RUTA_ID);
        assertThat(result.getValue().content().get(0).severidad()).isEqualTo("MEDIA");
        assertThat(result.getValue().content().get(0).estado()).isEqualTo("ABIERTO");
    }

    @Test
    void listar_sinDesvios_retornaPaginaVacia() {
        PageResult<DesvioView> page = PageResult.empty(0, 20);
        when(desvioRutaPersistencePort.findActivosByRuta(any(), any(), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<DesvioRutaResponseDto>> result = handler.listar(
                new ListarDesviosActivosQuery(RUTA_ID, TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
