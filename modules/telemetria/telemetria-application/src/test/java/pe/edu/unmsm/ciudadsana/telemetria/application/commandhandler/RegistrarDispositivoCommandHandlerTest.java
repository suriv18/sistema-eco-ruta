package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarDispositivoCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DispositivoResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DispositivosPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.TelemetriaEventPublisherPort;
import pe.edu.unmsm.ciudadsana.telemetria.domain.model.DispositivoGps;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.EstadoDispositivo;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarDispositivoCommandHandlerTest {

    @Mock
    DispositivosPersistencePort dispositivosPersistencePort;

    @Mock
    TelemetriaEventPublisherPort eventPublisher;

    @InjectMocks
    RegistrarDispositivoCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID UNIDAD_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID DISPOSITIVO_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    private RegistrarDispositivoCommand comando() {
        return new RegistrarDispositivoCommand(TENANT_ID, UNIDAD_ID, "IMEI-001", "PROVEEDOR-X");
    }

    @Test
    void registrar_unidadSinDispositivo_retornaDto() {
        DispositivoGps dispositivo = mock(DispositivoGps.class);
        when(dispositivo.getId()).thenReturn(DispositivoId.of(DISPOSITIVO_ID));
        when(dispositivo.getTenantId()).thenReturn(TenantId.of(TENANT_ID));
        when(dispositivo.getUnidadExternoId()).thenReturn(UnidadExternoId.of(UNIDAD_ID));
        when(dispositivo.getImei()).thenReturn(Optional.of("IMEI-001"));
        when(dispositivo.getProveedor()).thenReturn(Optional.of("PROVEEDOR-X"));
        when(dispositivo.getEstado()).thenReturn(EstadoDispositivo.ACTIVO);
        when(dispositivo.getUltimoPingEn()).thenReturn(Optional.empty());
        when(dispositivo.getCreadoEn()).thenReturn(Instant.now());
        when(dispositivo.pullDomainEvents()).thenReturn(Collections.emptyList());

        when(dispositivosPersistencePort.existsByUnidadExternoIdAndTenantId(any(), any()))
                .thenReturn(false);
        when(dispositivosPersistencePort.save(any())).thenReturn(dispositivo);

        Result<DispositivoResponseDto> result = handler.registrar(comando());

        assertThat(result.isSuccess()).isTrue();
        DispositivoResponseDto dto = result.getValue();
        assertThat(dto.id()).isEqualTo(DISPOSITIVO_ID);
        assertThat(dto.tenantId()).isEqualTo(TENANT_ID);
        assertThat(dto.unidadExternoId()).isEqualTo(UNIDAD_ID);
        assertThat(dto.imei()).isEqualTo("IMEI-001");
        assertThat(dto.proveedor()).isEqualTo("PROVEEDOR-X");
        assertThat(dto.estado()).isEqualTo("ACTIVO");
    }

    @Test
    void registrar_unidadYaTieneDispositivo_retornaError() {
        when(dispositivosPersistencePort.existsByUnidadExternoIdAndTenantId(any(), any()))
                .thenReturn(true);

        Result<DispositivoResponseDto> result = handler.registrar(comando());

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }
}
