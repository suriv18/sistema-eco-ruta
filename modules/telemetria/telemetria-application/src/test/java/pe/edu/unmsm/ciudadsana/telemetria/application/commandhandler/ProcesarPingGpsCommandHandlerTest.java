package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.ProcesarPingGpsCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.PingGpsResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DispositivosPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.EstadoUnidadPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.PingGpsPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.TelemetriaEventPublisherPort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.PingGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.PingId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.OrigenPing;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcesarPingGpsCommandHandlerTest {

    @Mock
    DispositivosPersistencePort dispositivosPersistencePort;

    @Mock
    PingGpsPersistencePort pingGpsPersistencePort;

    @Mock
    EstadoUnidadPersistencePort estadoUnidadPersistencePort;

    @Mock
    TelemetriaEventPublisherPort eventPublisher;

    @InjectMocks
    ProcesarPingGpsCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DISPOSITIVO_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID UNIDAD_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID PING_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    private ProcesarPingGpsCommand comando() {
        return new ProcesarPingGpsCommand(
                TENANT_ID, DISPOSITIVO_ID, UNIDAD_ID, null,
                Instant.now(), -12.046374, -77.042793,
                60.0, 180.0, 5.0, "GPS_REAL"
        );
    }

    @Test
    void procesar_dispositivoExistente_retornaPingDto() {
        DispositivoGps dispositivo = mock(DispositivoGps.class);

        PingGps ping = mock(PingGps.class);
        when(ping.getId()).thenReturn(PingId.of(PING_ID));
        when(ping.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(ping.getDispositivoId()).thenReturn(DispositivoId.of(DISPOSITIVO_ID));
        when(ping.getUnidadExternoId()).thenReturn(UnidadExternoId.of(UNIDAD_ID));
        when(ping.getRutaExternoId()).thenReturn(Optional.empty());
        when(ping.getTs()).thenReturn(Instant.now());
        when(ping.getPosicion()).thenReturn(Coordenadas.of(-12.046374, -77.042793));
        when(ping.getVelocidadKmh()).thenReturn(Optional.of(60.0));
        when(ping.getRumboGrados()).thenReturn(Optional.of(180.0));
        when(ping.getPrecisionM()).thenReturn(Optional.of(5.0));
        when(ping.getOrigen()).thenReturn(OrigenPing.GPS_REAL);
        when(ping.getRecibidoEn()).thenReturn(Instant.now());
        when(ping.pullDomainEvents()).thenReturn(Collections.emptyList());

        when(dispositivosPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.of(dispositivo));
        when(pingGpsPersistencePort.save(any())).thenReturn(ping);
        when(dispositivosPersistencePort.save(any())).thenReturn(dispositivo);
        when(estadoUnidadPersistencePort.findByUnidad(any(), any())).thenReturn(Optional.empty());

        Result<PingGpsResponseDto> result = handler.procesar(comando());

        assertThat(result.isSuccess()).isTrue();
        PingGpsResponseDto dto = result.getValue();
        assertThat(dto.id()).isEqualTo(PING_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.dispositivoId()).isEqualTo(DISPOSITIVO_ID);
        assertThat(dto.unidadExternoId()).isEqualTo(UNIDAD_ID);
        assertThat(dto.origen()).isEqualTo("GPS_REAL");
    }

    @Test
    void procesar_dispositivoNoExiste_retornaNoEncontrado() {
        when(dispositivosPersistencePort.findByIdAndTenantId(any(), any()))
                .thenReturn(Optional.empty());

        Result<PingGpsResponseDto> result = handler.procesar(comando());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RECURSO_NO_ENCONTRADO);
    }
}
