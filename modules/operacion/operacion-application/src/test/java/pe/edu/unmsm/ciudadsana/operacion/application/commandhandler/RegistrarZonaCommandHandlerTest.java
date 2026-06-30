package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarZonaCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ZonasPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Zona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CodigoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.PrioridadBase;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarZonaCommandHandlerTest {

    @Mock
    private DistritosPersistencePort distritosPersistencePort;

    @Mock
    private ZonasPersistencePort zonasPersistencePort;

    @Mock
    private OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    private RegistrarZonaCommandHandler handler;

    @Test
    void registrar_zona_valida_retorna_success() {
        UUID tenantId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        RegistrarZonaCommand command = new RegistrarZonaCommand(
            tenantId, distritoId, "ZN-001", "Zona Norte", "RESIDENCIAL", 1
        );
        Distrito distritoMock = mock(Distrito.class);
        Zona zonaGuardada = Zona.reconstitute(
            ZonaId.of(UUID.randomUUID()),
            TenantId.of(tenantId),
            DistritoId.of(distritoId),
            CodigoZona.of("ZN-001"),
            "Zona Norte",
            TipoZona.RESIDENCIAL,
            PrioridadBase.of(1),
            EstadoZona.ACTIVA,
            Instant.now()
        );
        when(distritosPersistencePort.findById(any(DistritoId.class), any(TenantId.class)))
            .thenReturn(Optional.of(distritoMock));
        when(zonasPersistencePort.existsByCodigo(any(CodigoZona.class), any(TenantId.class))).thenReturn(false);
        when(zonasPersistencePort.save(any(Zona.class))).thenReturn(zonaGuardada);
        doNothing().when(eventPublisher).publishAll(any(List.class));

        Result<ZonaResponseDto> result = handler.registrar(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().codigo()).isEqualTo("ZN-001");
        assertThat(result.getValue().nombre()).isEqualTo("Zona Norte");
        assertThat(result.getValue().estado()).isEqualTo("ACTIVA");
    }

    @Test
    void registrar_zona_distrito_no_encontrado_retorna_distrito_no_encontrado() {
        UUID tenantId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        RegistrarZonaCommand command = new RegistrarZonaCommand(
            tenantId, distritoId, "ZN-002", "Zona Sur", "COMERCIAL", 2
        );
        when(distritosPersistencePort.findById(any(DistritoId.class), any(TenantId.class)))
            .thenReturn(Optional.empty());

        Result<ZonaResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.DISTRITO_NO_ENCONTRADO);
    }

    @Test
    void registrar_zona_codigo_duplicado_retorna_validacion_error() {
        UUID tenantId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        RegistrarZonaCommand command = new RegistrarZonaCommand(
            tenantId, distritoId, "ZN-001", "Zona Este", "MIXTA", 3
        );
        Distrito distritoMock = mock(Distrito.class);
        when(distritosPersistencePort.findById(any(DistritoId.class), any(TenantId.class)))
            .thenReturn(Optional.of(distritoMock));
        when(zonasPersistencePort.existsByCodigo(any(CodigoZona.class), any(TenantId.class))).thenReturn(true);

        Result<ZonaResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }

    @Test
    void registrar_zona_tipo_invalido_retorna_validacion_error() {
        UUID tenantId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        RegistrarZonaCommand command = new RegistrarZonaCommand(
            tenantId, distritoId, "ZN-003", "Zona X", "TIPO_INVALIDO", 1
        );
        Distrito distritoMock = mock(Distrito.class);
        when(distritosPersistencePort.findById(any(DistritoId.class), any(TenantId.class)))
            .thenReturn(Optional.of(distritoMock));
        when(zonasPersistencePort.existsByCodigo(any(CodigoZona.class), any(TenantId.class))).thenReturn(false);

        Result<ZonaResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }
}
