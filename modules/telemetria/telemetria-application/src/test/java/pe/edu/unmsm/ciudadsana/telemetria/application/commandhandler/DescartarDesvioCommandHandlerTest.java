package pe.edu.unmsm.ciudadsana.telemetria.application.commandhandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.DescartarDesvioCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.DesvioRutaPersistencePort.DesvioView;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DescartarDesvioCommandHandlerTest {

    @Mock
    DesvioRutaPersistencePort desvioRutaPersistencePort;

    @InjectMocks
    DescartarDesvioCommandHandler handler;

    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DESVIO_ID = UUID.fromString("55555555-5555-5555-5555-555555555555");
    private static final UUID UNIDAD_ID = UUID.fromString("66666666-6666-6666-6666-666666666666");
    private static final UUID RUTA_ID = UUID.fromString("77777777-7777-7777-7777-777777777777");

    private DesvioView desvioAbierto() {
        return new DesvioView(
                DESVIO_ID, TENANT_ID, UNIDAD_ID, RUTA_ID,
                -12.046374, -77.042793, 200.0,
                "ALTA", "ABIERTO", Instant.now().minusSeconds(60), null
        );
    }

    @Test
    void descartar_desvioAbierto_retornaSuccess() {
        when(desvioRutaPersistencePort.findById(any(), any()))
                .thenReturn(Optional.of(desvioAbierto()));
        when(desvioRutaPersistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Result<Void> result = handler.descartar(new DescartarDesvioCommand(DESVIO_ID, TENANT_ID));

        assertThat(result.isSuccess()).isTrue();
        verify(desvioRutaPersistencePort).save(any());
    }

    @Test
    void descartar_desvioNoExiste() {
        when(desvioRutaPersistencePort.findById(any(), any()))
                .thenReturn(Optional.empty());

        Result<Void> result = handler.descartar(new DescartarDesvioCommand(DESVIO_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.RECURSO_NO_ENCONTRADO);
    }

    @Test
    void descartar_desvioNoAbierto_retornaOperacionNoPermitida() {
        DesvioView desvioDescartado = new DesvioView(
                DESVIO_ID, TENANT_ID, UNIDAD_ID, RUTA_ID,
                -12.046374, -77.042793, 200.0,
                "ALTA", "DESCARTADO", Instant.now().minusSeconds(120), null
        );
        when(desvioRutaPersistencePort.findById(any(), any()))
                .thenReturn(Optional.of(desvioDescartado));

        Result<Void> result = handler.descartar(new DescartarDesvioCommand(DESVIO_ID, TENANT_ID));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError().code()).isEqualTo(ErrorCode.OPERACION_NO_PERMITIDA);
    }
}
