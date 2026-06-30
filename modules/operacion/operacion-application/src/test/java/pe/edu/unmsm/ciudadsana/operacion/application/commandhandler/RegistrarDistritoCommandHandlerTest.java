package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarDistritoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDistrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarDistritoCommandHandlerTest {

    @Mock
    private DistritosPersistencePort distritosPersistencePort;

    @Mock
    private OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    private RegistrarDistritoCommandHandler handler;

    @Test
    void registrar_distrito_valido_retorna_success() {
        UUID tenantId = UUID.randomUUID();
        RegistrarDistritoCommand command = new RegistrarDistritoCommand(tenantId, "Miraflores", "150122");
        Distrito distritoGuardado = Distrito.reconstitute(
            DistritoId.of(UUID.randomUUID()),
            TenantId.of(tenantId),
            "Miraflores",
            "150122",
            EstadoDistrito.ACTIVO,
            Instant.now()
        );
        when(distritosPersistencePort.existsByNombre(eq("Miraflores"), any(TenantId.class))).thenReturn(false);
        when(distritosPersistencePort.save(any(Distrito.class))).thenReturn(distritoGuardado);
        doNothing().when(eventPublisher).publishAll(any(List.class));

        Result<DistritoResponseDto> result = handler.registrar(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().nombre()).isEqualTo("Miraflores");
        assertThat(result.getValue().estado()).isEqualTo("ACTIVO");
    }

    @Test
    void registrar_distrito_sin_ubigeo_retorna_success() {
        UUID tenantId = UUID.randomUUID();
        RegistrarDistritoCommand command = new RegistrarDistritoCommand(tenantId, "San Isidro", null);
        Distrito distritoGuardado = Distrito.reconstitute(
            DistritoId.of(UUID.randomUUID()),
            TenantId.of(tenantId),
            "San Isidro",
            null,
            EstadoDistrito.ACTIVO,
            Instant.now()
        );
        when(distritosPersistencePort.existsByNombre(eq("San Isidro"), any(TenantId.class))).thenReturn(false);
        when(distritosPersistencePort.save(any(Distrito.class))).thenReturn(distritoGuardado);
        doNothing().when(eventPublisher).publishAll(any(List.class));

        Result<DistritoResponseDto> result = handler.registrar(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().ubigeo()).isNull();
    }

    @Test
    void registrar_distrito_nombre_duplicado_retorna_validacion_error() {
        UUID tenantId = UUID.randomUUID();
        RegistrarDistritoCommand command = new RegistrarDistritoCommand(tenantId, "Miraflores", "150122");
        when(distritosPersistencePort.existsByNombre(eq("Miraflores"), any(TenantId.class))).thenReturn(true);

        Result<DistritoResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }
}
