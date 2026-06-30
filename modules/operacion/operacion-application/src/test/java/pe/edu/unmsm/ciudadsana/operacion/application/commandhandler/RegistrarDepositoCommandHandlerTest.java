package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarDepositoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DepositosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Deposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Distrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarDepositoCommandHandlerTest {

    @Mock
    private DistritosPersistencePort distritosPersistencePort;

    @Mock
    private DepositosPersistencePort depositosPersistencePort;

    @InjectMocks
    private RegistrarDepositoCommandHandler handler;

    @Test
    void registrar_deposito_valido_retorna_success() {
        UUID tenantId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        RegistrarDepositoCommand command = new RegistrarDepositoCommand(
            tenantId, distritoId, "Depósito Norte", "BASE"
        );
        Distrito distritoMock = mock(Distrito.class);
        Deposito depositoEsperado = Deposito.reconstitute(
            DepositoId.of(UUID.randomUUID()),
            TenantId.of(tenantId),
            DistritoId.of(distritoId),
            "Depósito Norte",
            TipoDeposito.BASE,
            EstadoDeposito.ACTIVO,
            Instant.now()
        );
        when(distritosPersistencePort.findById(any(DistritoId.class), any(TenantId.class)))
            .thenReturn(Optional.of(distritoMock));
        doNothing().when(depositosPersistencePort).save(any(Deposito.class));

        Result<DepositoResponseDto> result = handler.registrar(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().nombre()).isEqualTo("Depósito Norte");
        assertThat(result.getValue().tipo()).isEqualTo("BASE");
        assertThat(result.getValue().estado()).isEqualTo("ACTIVO");
    }

    @Test
    void registrar_deposito_distrito_no_encontrado_retorna_distrito_no_encontrado() {
        UUID tenantId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        RegistrarDepositoCommand command = new RegistrarDepositoCommand(
            tenantId, distritoId, "Depósito Sur", "TRANSFERENCIA"
        );
        when(distritosPersistencePort.findById(any(DistritoId.class), any(TenantId.class)))
            .thenReturn(Optional.empty());

        Result<DepositoResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.DISTRITO_NO_ENCONTRADO);
    }

    @Test
    void registrar_deposito_tipo_invalido_retorna_validacion_error() {
        UUID tenantId = UUID.randomUUID();
        UUID distritoId = UUID.randomUUID();
        RegistrarDepositoCommand command = new RegistrarDepositoCommand(
            tenantId, distritoId, "Depósito X", "TIPO_INEXISTENTE"
        );
        Distrito distritoMock = mock(Distrito.class);
        when(distritosPersistencePort.findById(any(DistritoId.class), any(TenantId.class)))
            .thenReturn(Optional.of(distritoMock));

        Result<DepositoResponseDto> result = handler.registrar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
    }
}
