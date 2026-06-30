package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DispositivoResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DispositivosPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarDispositivosQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.EstadoDispositivo;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarDispositivosQueryHandlerTest {

    @Mock
    DispositivosPersistencePort dispositivosPersistencePort;

    @InjectMocks
    ListarDispositivosQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private DispositivoGps dispositivo() {
        return DispositivoGps.reconstitute(
                DispositivoId.of(UUID.randomUUID()), TenantId.of(TENANT_ID),
                UnidadExternoId.of(UUID.randomUUID()),
                Optional.empty(), Optional.of("Teltonika"),
                EstadoDispositivo.ACTIVO, Optional.empty(), Instant.now());
    }

    @Test
    void listar_conDispositivos_retornaPaginaConDtos() {
        PageResult<DispositivoGps> page = PageResult.of(List.of(dispositivo(), dispositivo()), 0, 20, 2L);
        when(dispositivosPersistencePort.findAllByTenantId(any(TenantId.class), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<DispositivoResponseDto>> result = handler.listar(
                new ListarDispositivosQuery(TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).hasSize(2);
        assertThat(result.getValue().totalElements()).isEqualTo(2L);
        assertThat(result.getValue().content().get(0).estado()).isEqualTo("ACTIVO");
    }

    @Test
    void listar_sinDispositivos_retornaPaginaVacia() {
        PageResult<DispositivoGps> page = PageResult.empty(0, 20);
        when(dispositivosPersistencePort.findAllByTenantId(any(TenantId.class), anyInt(), anyInt())).thenReturn(page);

        Result<PageResult<DispositivoResponseDto>> result = handler.listar(
                new ListarDispositivosQuery(TENANT_ID, 0, 20));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().content()).isEmpty();
    }
}
