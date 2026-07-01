package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarDepositoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DepositosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Deposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DesactivarDepositoCommandHandlerTest {

    @Mock
    DepositosPersistencePort depositosPersistencePort;

    @InjectMocks
    DesactivarDepositoCommandHandler handler;

    // Municipalidad Metropolitana de Lima (tenant)
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    // Estación de Transferencia Norte (Carabayllo) - punto de acopio antes de relleno sanitario
    private static final UUID DEPOSITO_NORTE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void desactivar_deposito_activo_retorna_success() {
        Deposito deposito = mock(Deposito.class);
        when(depositosPersistencePort.findByIdAndTenantId(DepositoId.of(DEPOSITO_NORTE_ID), TenantId.of(TENANT_ID)))
                .thenReturn(Optional.of(deposito));

        DesactivarDepositoCommand command = new DesactivarDepositoCommand(DEPOSITO_NORTE_ID, TENANT_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isSuccess()).isTrue();
        verify(deposito).desactivar();
        verify(depositosPersistencePort).save(deposito);
    }

    @Test
    void desactivar_deposito_no_encontrado_retorna_failure() {
        when(depositosPersistencePort.findByIdAndTenantId(any(), any())).thenReturn(Optional.empty());

        DesactivarDepositoCommand command = new DesactivarDepositoCommand(DEPOSITO_NORTE_ID, TENANT_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.DEPOSITO_NO_ENCONTRADO);
        verify(depositosPersistencePort, never()).save(any());
    }

    @Test
    void desactivar_deposito_ya_inactivo_retorna_validacion_error() {
        Deposito deposito = mock(Deposito.class);
        when(depositosPersistencePort.findByIdAndTenantId(any(), any())).thenReturn(Optional.of(deposito));
        doThrow(new IllegalStateException("El deposito ya está INACTIVO"))
                .when(deposito).desactivar();

        DesactivarDepositoCommand command = new DesactivarDepositoCommand(DEPOSITO_NORTE_ID, TENANT_ID);

        Result<Void> result = handler.desactivar(command);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.VALIDACION_ERROR);
        verify(depositosPersistencePort, never()).save(any());
    }
}
