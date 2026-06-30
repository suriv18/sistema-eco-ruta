package pe.edu.unmsm.ciudadsana.telemetria.application.queryhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DispositivoResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DispositivosPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ObtenerDispositivoQuery;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.EstadoDispositivo;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerDispositivoQueryHandlerTest {

    @Mock
    DispositivosPersistencePort dispositivosPersistencePort;

    @InjectMocks
    ObtenerDispositivoQueryHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISPOSITIVO_ID = UUID.randomUUID();
    private static final UUID UNIDAD_ID = UUID.randomUUID();

    private DispositivoGps dispositivo() {
        return DispositivoGps.reconstitute(
                DispositivoId.of(DISPOSITIVO_ID), TenantId.of(TENANT_ID),
                UnidadExternoId.of(UNIDAD_ID),
                Optional.of("IMEI-12345"), Optional.of("Teltonika"),
                EstadoDispositivo.ACTIVO, Optional.empty(), Instant.now());
    }

    @Test
    void obtener_dispositivoExistente_retornaDto() {
        when(dispositivosPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.of(dispositivo()));

        Result<DispositivoResponseDto> result = handler.obtener(
                new ObtenerDispositivoQuery(DISPOSITIVO_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().id()).isEqualTo(DISPOSITIVO_ID);
        assertThat(result.getValue().tenantId()).isEqualTo(TENANT_ID);
        assertThat(result.getValue().unidadExternoId()).isEqualTo(UNIDAD_ID);
        assertThat(result.getValue().imei()).isEqualTo("IMEI-12345");
        assertThat(result.getValue().estado()).isEqualTo("ACTIVO");
    }

    @Test
    void obtener_dispositivoNoExiste_retornaRecursoNoEncontrado() {
        when(dispositivosPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.empty());

        Result<DispositivoResponseDto> result = handler.obtener(
                new ObtenerDispositivoQuery(DISPOSITIVO_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RECURSO_NO_ENCONTRADO);
    }
}
