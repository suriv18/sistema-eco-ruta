package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EstadoUnidadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort.EstadoUnidadView;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ObtenerEstadoUnidadQuery;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerEstadoUnidadQueryHandlerTest {

    @Mock
    EstadoUnidadPersistencePort estadoUnidadPersistencePort;

    @InjectMocks
    ObtenerEstadoUnidadQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNIDAD_ID = UUID.randomUUID();

    private EstadoUnidadView estadoView() {
        return new EstadoUnidadView(UUID.randomUUID(), TENANT_ID, UNIDAD_ID,
                null, -12.046, -77.042, 45.0, Instant.now(), "EN_RUTA", Instant.now());
    }

    @Test
    void obtener_estadoExistente_retornaDto() {
        when(estadoUnidadPersistencePort.findByUnidad(any(), any()))
                .thenReturn(Optional.of(estadoView()));

        Result<EstadoUnidadResponseDto> result = handler.obtener(
                new ObtenerEstadoUnidadQuery(UNIDAD_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().unidadExternoId()).isEqualTo(UNIDAD_ID);
        assertThat(result.getValue().tenantId()).isEqualTo(TENANT_ID);
        assertThat(result.getValue().estadoMovimiento()).isEqualTo("EN_RUTA");
    }

    @Test
    void obtener_unidadNoEncontrada_retornaRecursoNoEncontrado() {
        when(estadoUnidadPersistencePort.findByUnidad(any(), any()))
                .thenReturn(Optional.empty());

        Result<EstadoUnidadResponseDto> result = handler.obtener(
                new ObtenerEstadoUnidadQuery(UNIDAD_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RECURSO_NO_ENCONTRADO);
    }
}
