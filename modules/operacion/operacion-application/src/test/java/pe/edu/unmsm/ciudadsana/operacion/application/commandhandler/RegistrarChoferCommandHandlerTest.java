package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarChoferCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoChofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
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
class RegistrarChoferCommandHandlerTest {

    @Mock
    private ChoferesPersistencePort choferesPersistencePort;

    @Mock
    private OperacionEventPublisherPort eventPublisher;

    @InjectMocks
    private RegistrarChoferCommandHandler handler;

    @Test
    void registrar_chofer_valido_retorna_success() {
        UUID tenantId = UUID.randomUUID();
        RegistrarChoferCommand command = new RegistrarChoferCommand(
            tenantId, "Juan", "Pérez", "12345678", "A-IIb-12345", "999000111"
        );
        Chofer choferGuardado = Chofer.reconstitute(
            ChoferId.of(UUID.randomUUID()),
            TenantId.of(tenantId),
            "Juan", "Pérez",
            "12345678", "A-IIb-12345", "999000111",
            EstadoChofer.ACTIVO,
            Instant.now()
        );
        when(choferesPersistencePort.existsByDni(eq("12345678"), any(TenantId.class))).thenReturn(false);
        when(choferesPersistencePort.save(any(Chofer.class))).thenReturn(choferGuardado);
        doNothing().when(eventPublisher).publishAll(any(List.class));

        Result<ChoferResponseDto> result = handler.registrar(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().nombres()).isEqualTo("Juan");
        assertThat(result.getValue().apellidos()).isEqualTo("Pérez");
        assertThat(result.getValue().estado()).isEqualTo("ACTIVO");
    }

    @Test
    void registrar_chofer_sin_dni_retorna_success() {
        UUID tenantId = UUID.randomUUID();
        RegistrarChoferCommand command = new RegistrarChoferCommand(
            tenantId, "Ana", "López", null, null, null
        );
        Chofer choferGuardado = Chofer.reconstitute(
            ChoferId.of(UUID.randomUUID()),
            TenantId.of(tenantId),
            "Ana", "López",
            null, null, null,
            EstadoChofer.ACTIVO,
            Instant.now()
        );
        when(choferesPersistencePort.save(any(Chofer.class))).thenReturn(choferGuardado);
        doNothing().when(eventPublisher).publishAll(any(List.class));

        Result<ChoferResponseDto> result = handler.registrar(command);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void registrar_chofer_con_dni_duplicado_retorna_validacion_error() {
        UUID tenantId = UUID.randomUUID();
        RegistrarChoferCommand command = new RegistrarChoferCommand(
            tenantId, "Carlos", "Ruiz", "12345678", null, null
        );
        when(choferesPersistencePort.existsByDni(eq("12345678"), any(TenantId.class))).thenReturn(true);

        Result<ChoferResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }
}
